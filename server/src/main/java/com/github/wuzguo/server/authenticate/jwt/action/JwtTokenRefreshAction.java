package com.github.wuzguo.server.authenticate.jwt.action;


import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.github.wuzguo.server.action.AbstractActionDataListener;
import com.github.wuzguo.server.authenticate.jwt.util.JwtUtil;
import com.github.wuzguo.webpush.common.annotation.Comment;
import com.github.wuzguo.webpush.common.annotation.ParametersComment;
import com.github.wuzguo.webpush.common.annotation.ReturnComment;
import com.github.wuzguo.webpush.common.enums.MessageTypeEnum;
import com.github.wuzguo.webpush.common.vo.BusinessP2PMessageVo;
import com.github.wuzguo.webpush.common.vo.JwtTokenVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * jwt令牌刷新
 *
 * @author wuzguo
 * @date 2016年12月18日 下午3:05:24
 */
@Component
@Comment(value = "JWT令牌ws接口",
        protocol = "ws",
        url = "ws://ip:port/",
        style = "Web Socket",
        provider = "MPS")
public class JwtTokenRefreshAction extends AbstractActionDataListener<JwtTokenVo> {
    /**
     * 日志
     */
    protected static final Logger logger = LoggerFactory.getLogger(JwtTokenRefreshAction.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${message.action.jwt.token.refresh.actionName:/jwt/token/refresh}")
    private String actionName;

    @Override
    public String getActionName() {
        return actionName;
    }

    @Override
    public Class getDataClass() {
        return JwtTokenVo.class;
    }

    @Override
    @Comment(value = "刷新JWT令牌ws接口",
            url = "/jwt/token/refresh",
            sampleReq = JwtTokenVo.class,
            sampleRes = BusinessP2PMessageVo.class,
            business = "旧令牌在未失效之前并在令牌刷新期间,根据原来的用户信息产生新的令牌。若不在刷新期间返回原令牌.<br>event为bizp2p。")
    @ParametersComment(name = "JSON String", value = "令牌参数", required = true, detail = JwtTokenVo.class)
    @ReturnComment(type = JwtTokenVo.class, value = "data", isArray = false)
    public void onData(final SocketIOClient client, final JwtTokenVo tokenVo, final AckRequest req) throws Exception {
        String newToken = null;
        String token = tokenVo.getToken();
        try {
            newToken = jwtUtil.refresh(token);
        } catch (Exception e) {
            logger.error("刷新jwt签名失败", e);
            BusinessP2PMessageVo resVo = new BusinessP2PMessageVo();
            resVo.setStatus("0");
            resVo.setBiz(getActionName());
            resVo.setTime(System.currentTimeMillis());
            resVo.setError(e.getMessage());
            resVo.setData("刷新jwt签名失败");
            //发送异常消息
            client.sendEvent(MessageTypeEnum.bizp2p.name(), resVo);
        }

        JwtTokenVo newTokenVo = new JwtTokenVo(newToken, !newToken.equals(token), JwtUtil.lastExpires(newToken) / 1000);

        BusinessP2PMessageVo resVo = new BusinessP2PMessageVo();
        resVo.setStatus("1");
        resVo.setData(newTokenVo);
        resVo.setBiz(getActionName());
        resVo.setTime(System.currentTimeMillis());
        //发送成功消息
        client.sendEvent(MessageTypeEnum.bizp2p.name(), resVo);
    }

}
