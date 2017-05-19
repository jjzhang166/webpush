package com.github.wuzguo.server.server;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.github.wuzguo.server.action.ActionDataListener;
import com.github.wuzguo.server.listener.NettySocketIoConnectOrDisconnectListener;
import com.github.wuzguo.webpush.common.cluster.IClusterMessageSend;
import com.github.wuzguo.webpush.common.cluster.IClusterSession;
import com.github.wuzguo.webpush.common.enums.MessageTypeEnum;
import com.github.wuzguo.webpush.common.filcker.IConnectionFilckerMessageCached;
import com.github.wuzguo.webpush.common.server.ISocketIoServer;
import com.github.wuzguo.webpush.common.vo.BaseMessageVo;
import com.github.wuzguo.webpush.common.vo.GroupMessageVo;
import com.github.wuzguo.webpush.common.vo.SystemMessageVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * netty-socketio server
 *
 * @author wuzguo
 * @date 2016年12月16日 下午12:27:02
 */
@Component
public class NettySocketIoServer implements ISocketIoServer {
    /**
     * 日志
     */
    protected static final Logger logger = LoggerFactory.getLogger(NettySocketIoServer.class);

    /**
     * 服务配置
     **/
    @Autowired
    private Configuration config;

    /**
     * 服务对象
     **/
    private SocketIOServer server;

    /**
     * 消息动作处理
     **/
    @Autowired(required = false)
    private List<ActionDataListener> actionDataListeners;

    /**
     * 连接开关事件处理
     **/
    @Autowired(required = false)
    private NettySocketIoConnectOrDisconnectListener connectOrDisconnectListener;

    /**
     * 连接闪断消息缓存
     **/
    @Autowired(required = false)
    private IConnectionFilckerMessageCached connectionFilckerMessageCached;

    /**
     * 集群消息发送
     **/
    @Autowired(required = false)
    private IClusterMessageSend clusterMessageSend;

    @Autowired(required = false)
    private IClusterSession clusterSession;


    public Configuration getConfig() {
        return config;
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }

    public SocketIOServer getServer() {
        return server;
    }

    public void setServer(SocketIOServer server) {
        this.server = server;
    }

    public List<ActionDataListener> getActionDataListeners() {
        return actionDataListeners;
    }

    /**
     * @param actionDataListeners
     */
    public void setActionDataListeners(final List<ActionDataListener> actionDataListeners) {
        this.actionDataListeners = actionDataListeners;
    }

    public NettySocketIoConnectOrDisconnectListener getConOrDisListener() {
        return connectOrDisconnectListener;
    }

    /**
     * @param conOrDisListener
     */
    public void setConOrDisListener(final NettySocketIoConnectOrDisconnectListener conOrDisListener) {
        this.connectOrDisconnectListener = conOrDisListener;
    }


    /**
     * 启动netty-socketio server
     */
    @Override
    public void run() {
        logger.info("netty socket io server init config, host name:{}, port:{}, context:{}...", config.getHostname(), config.getPort(), config.getContext());
        server = new SocketIOServer(config);

        if (connectOrDisconnectListener != null) {
            server.addConnectListener(connectOrDisconnectListener);
            server.addDisconnectListener(connectOrDisconnectListener);
            logger.info("netty socket io server, connect or disconnect listener is class:{}.", connectOrDisconnectListener.getClass().getName());
        } else {
            logger.info("netty socket io server, connect or disconnect listener is null.");
        }

        if (actionDataListeners != null) {
            for (ActionDataListener actionDataListener : actionDataListeners) {
                server.addEventListener(actionDataListener.getActionName()
                        , actionDataListener.getDataClass()
                        , actionDataListener);
                logger.info("netty socket io server, add action data listener name:{}, data class:{}, action class:{}."
                        , actionDataListener.getActionName()
                        , actionDataListener.getDataClass().getName()
                        , actionDataListener.getClass().getName());
            }
        } else {
            logger.info("netty socket io server, action data listener is null.");
        }

        server.start();
        logger.info("netty socket io server start success.");
    }

    /**
     * 关闭  netty-socketio server
     */
    @Override
    public void stop() {
        if (server != null) {
            server.stop();
        }
    }

    /**
     * @param eventName
     * @param uuid
     * @param msgVo
     */
    @Override
    public void sendEvent(final String eventName, final UUID uuid, final BaseMessageVo msgVo) {
        SocketIOClient client = server.getClient(uuid);
        client.sendEvent(eventName, msgVo);
    }

    /**
     * @param msgType
     * @param uid
     * @param msgVo
     * @param cached  缓存
     * @param sync    消息同步
     * @return
     */
    @Override
    public String sendP2pMessage(final MessageTypeEnum msgType, final String uid, final BaseMessageVo msgVo, final boolean cached, final boolean sync) {
        // 获取用户的SessionId列表
        Set<UUID> uuids = SessionUUIDCached.get(uid);
        if (uuids == null) {
            if (sync && clusterSession != null && clusterSession.exists(uid)) {
                //集群模式先判断其他集群是否有这个会话，如果有，不缓存直接发送其他集群
                clusterMessageSend.p2pMessage(msgType, uid, msgVo);
                return "2";
            } else if (cached && connectionFilckerMessageCached != null) {
                //加入到缓存
                connectionFilckerMessageCached.add(uid, msgVo);
                return "2";
            } else {
                return "0";
            }
        } else {
            //支持多个发送
            for (UUID uuid : uuids) {
                sendEvent(msgType.name(), uuid, msgVo);
            }

            //发送到其他集群
            if (sync && clusterMessageSend != null) {
                clusterMessageSend.p2pMessage(msgType, uid, msgVo);
            }
        }

        return "1";
    }

    /**
     * @param groupId
     * @param msgVo
     * @param sync    消息同步
     * @return
     */
    @Override
    public String sendGroupMessage(final String groupId, final GroupMessageVo msgVo, final boolean sync) {
        BroadcastOperations group = server.getRoomOperations(groupId);
        if (group == null) {
            return "0";
        }
        group.sendEvent(MessageTypeEnum.group.name(), msgVo);

        //发送到其他集群
        if (sync && clusterMessageSend != null) {
            clusterMessageSend.groupMessage(groupId, msgVo);
        }

        return "1";
    }

    /**
     * @param msgVo
     * @param sync  消息同步
     */
    @Override
    public void sendSystemMessage(final SystemMessageVo msgVo, final boolean sync) {
        //发送到其他集群
        if (sync && clusterMessageSend != null) {
            clusterMessageSend.systemMessage(msgVo);
        } else {
            server.getBroadcastOperations().sendEvent(MessageTypeEnum.sys.name(), msgVo);
        }
    }

}
