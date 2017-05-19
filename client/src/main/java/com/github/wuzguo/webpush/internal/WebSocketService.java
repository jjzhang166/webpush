
package com.github.wuzguo.webpush.internal;

import com.alibaba.fastjson.JSONObject;
import com.github.wuzguo.webpush.common.exception.FrameException;
import com.github.wuzguo.webpush.common.util.HttpUtils;
import com.github.wuzguo.webpush.common.vo.JwtTokenUserVo;
import com.github.wuzguo.webpush.common.vo.P2PMessageVo;
import com.github.wuzguo.webpush.common.vo.ResultVo;
import com.github.wuzguo.webpush.pojo.SocketApiConfig;
import com.github.wuzguo.webpush.service.IWebSocketService;
import com.github.wuzguo.webpush.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO 调服务端的接口的服务实现类
 *
 * @author wuzguo at 2017/5/11 10:57
 */

@Service
public class WebSocketService implements IWebSocketService {
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketService.class);

    @Autowired
    private SocketApiConfig apiConfig;

    /**
     * @param user 用户对象
     * @return
     * @throws FrameException
     */
    @Override
    public ResultVo tokenGen(final User user) throws FrameException {
        // 创建 JwtTokenUserVo 对象
        JwtTokenUserVo tokenUser = new JwtTokenUserVo(user.getUid(), user.getName(), user.getRoles(), user.getGroups());
        return this.httpPost(tokenUser, apiConfig.getTokenGen());
    }

    /**
     * @param tokenVo token对象
     * @return
     * @throws FrameException
     */
    @Override
    public ResultVo refreshToken(final String token) throws FrameException {
        return this.httpPut(token, apiConfig.getRefreshToken());
    }

    /**
     * @param messageVo 消息体
     * @return
     * @throws FrameException
     */
    @Override
    public ResultVo sendP2PMessage(final P2PMessageVo messageVo, final String userId) throws FrameException {

        String sendUrl = apiConfig.getSendMessage().replace("{userId}", userId);
        System.out.println("sendUrl: " + sendUrl);
        return this.httpPost(messageVo, sendUrl);
    }


    /**
     * Post方法调用服务端的接口
     *
     * @param object
     * @param url
     * @return
     * @throws FrameException
     */
    private ResultVo httpPost(final Object object, final String url) throws FrameException {

        // 如果参数为空
        if (object == null) {
            LOGGER.warn("httpPost object is null");
            throw new IllegalArgumentException("object is null");
        }

        JSONObject objectJson = (JSONObject) JSONObject.toJSON(object);

        JSONObject resultJson = HttpUtils.post(url, new StringEntity(objectJson.toString(), ContentType.APPLICATION_JSON))
                .submitFastJson(0);

        LOGGER.info("httpPost resultJson: " + resultJson);

        // 如果为空
        if (resultJson == null || resultJson.isEmpty() || !resultJson.containsKey("data")) {
            throw new FrameException("httpPost resultJson is null");
        }

        // 如果不成功
        if (!StringUtils.equals("1", resultJson.getString("status"))) {
            throw new FrameException("httpPost failure");
        }

        // 获取Json数据
        ResultVo result = JSONObject.toJavaObject(resultJson, ResultVo.class);
        LOGGER.info("httpPost result: " + result);

        return result;
    }


    /**
     * Get方法调用服务端的接口
     *
     * @param token
     * @param url
     * @return
     * @throws FrameException
     */
    private ResultVo httpGet(final String token, final String url) throws FrameException {

        // 如果参数为空
        if (StringUtils.isEmpty(url)) {
            LOGGER.warn(" httpGet object is null");
            throw new IllegalArgumentException("object is null");
        }

        String urlTemp = url + "?token=" + token;
        LOGGER.info("httpGet url: " + urlTemp);

        JSONObject resultJson = HttpUtils.get(urlTemp, ContentType.APPLICATION_JSON)
                .submitFastJson(0);

        LOGGER.info("httpGet resultJson: " + resultJson);

        // 如果为空
        if (resultJson == null || resultJson.isEmpty() || !resultJson.containsKey("data")) {
            throw new FrameException("httpGet resultJson is null");
        }

        // 如果不成功
        if (!StringUtils.equals("1", resultJson.getString("status"))) {
            throw new FrameException("httpGet failure");
        }

        // 获取Json数据
        ResultVo result = JSONObject.toJavaObject(resultJson, ResultVo.class);
        LOGGER.info(" httpGet result: " + result);

        return result;
    }


    /**
     * Put方法调用服务端的接口
     *
     * @param token
     * @param url
     * @return
     * @throws FrameException
     */
    private ResultVo httpPut(final String token, final String url) throws FrameException {

        // 如果参数为空
        if (StringUtils.isEmpty(url) || StringUtils.isEmpty(token)) {
            LOGGER.warn("httpPut token or url is null");
            throw new IllegalArgumentException("httpPut token or url is null");
        }

        JSONObject resultJson = HttpUtils.put(url, new StringEntity(token, ContentType.APPLICATION_JSON))
                .submitFastJson(0);

        LOGGER.info("httpPut resultJson: " + resultJson);

        // 如果为空
        if (resultJson == null || resultJson.isEmpty() || !resultJson.containsKey("data")) {
            throw new FrameException("httpPut resultJson is null");
        }

        // 如果不成功
        if (!StringUtils.equals("1", resultJson.getString("status"))) {
            throw new FrameException("httpPut failure");
        }

        // 获取Json数据
        ResultVo result = JSONObject.toJavaObject(resultJson, ResultVo.class);
        LOGGER.info("httpPut result: " + result);
        return result;
    }
}
