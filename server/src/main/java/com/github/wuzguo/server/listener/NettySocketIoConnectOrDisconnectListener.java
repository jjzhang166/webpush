package com.github.wuzguo.server.listener;


import com.auth0.jwt.JWT;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.github.wuzguo.server.authenticate.jwt.util.JwtUtil;
import com.github.wuzguo.server.server.SessionUUIDCached;
import com.github.wuzguo.webpush.common.cluster.IClusterSession;
import com.github.wuzguo.webpush.common.enums.MessageTypeEnum;
import com.github.wuzguo.webpush.common.filcker.IConnectionFilckerMessageCached;
import com.github.wuzguo.webpush.common.server.ISocketIoServer;
import com.github.wuzguo.webpush.common.vo.BaseMessageVo;
import com.github.wuzguo.webpush.common.vo.BusinessP2PMessageVo;
import com.github.wuzguo.webpush.common.vo.JwtTokenUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * netty socket io 连接事件处理
 *
 * @author wuzguo
 * @date 2016年12月16日 下午2:05:54
 */
@Component
public class NettySocketIoConnectOrDisconnectListener implements ConnectListener, DisconnectListener {
    /**
     * 日志
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(NettySocketIoConnectOrDisconnectListener.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ISocketIoServer server;

    @Autowired(required = false)
    private IConnectionFilckerMessageCached connectionFilckerMessageCached;

    @Autowired(required = false)
    private IClusterSession clusterSession;

    /**
     * 客户端连接
     */
    @Override
    public void onConnect(final SocketIOClient client) {
        //校验令牌，如果令牌无效，返回异常信息并关闭连接。
        String token = client.getHandshakeData().getSingleUrlParam("token");
        JwtTokenUserVo userVo = null;
        try {
            JWT jwt = jwtUtil.verify(token);
            userVo = jwtUtil.getLoginUser(jwt);
        } catch (Exception e) {
            //校验不通过，断开连接
            LOGGER.error("连接令牌校验异常", e);
            BusinessP2PMessageVo bizMsgVo = new BusinessP2PMessageVo(null, "", "0", e.getMessage());
            bizMsgVo.setText("令牌校验失败");
            bizMsgVo.setBiz("token.verify");
            bizMsgVo.setTime(System.currentTimeMillis());
            client.sendEvent(MessageTypeEnum.bizp2p.name(), bizMsgVo);
            client.disconnect();
            return;
        }

        //缓存session
        client.set("user", userVo);
        SessionUUIDCached.add(userVo.getUid(), client.getSessionId());
        LOGGER.info("client connect uid:{}, session id:{}.", userVo.getUid(), client.getSessionId().toString());

        //添加到会话集群
        if (clusterSession != null) {
            clusterSession.open(userVo.getUid());
        }

        //加入群组
        if (userVo.getGroups() != null) {
            for (String group : userVo.getGroups()) {
                client.joinRoom(group);
            }
        }

        //发送缓存消息
        sendCachedMessage(userVo.getUid(), client);
    }

    /**
     * 客户端关闭
     */
    @Override
    public void onDisconnect(final SocketIOClient client) {
        //移除缓存session
        JwtTokenUserVo userVo = client.get("user");

        if (userVo == null) {
            return;
        }

        LOGGER.info("client disconnect uid:{}, session id:{}.", userVo.getUid(), client.getSessionId().toString());
        SessionUUIDCached.remove(userVo.getUid(), client.getSessionId());

        //会话从集群移除
        if (clusterSession != null) {
            clusterSession.close(userVo.getUid());
        }
    }

    /**
     * 发送缓存消息
     *
     * @param uId
     * @param client
     */
    private void sendCachedMessage(final String uId, final SocketIOClient client) {
        if (connectionFilckerMessageCached != null) {
            List<BaseMessageVo> msgs = connectionFilckerMessageCached.remove(uId);
            for (BaseMessageVo msgVo : msgs) {
                MessageTypeEnum en = MessageTypeEnum.getMessageTypeEnum(msgVo.getClass());
                server.sendP2pMessage(en, uId, msgVo, false, true);
            }
        }
    }
}
