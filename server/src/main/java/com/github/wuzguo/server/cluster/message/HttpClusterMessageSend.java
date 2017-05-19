package com.github.wuzguo.server.cluster.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.wuzguo.webpush.common.cluster.IClusterMessageSend;
import com.github.wuzguo.webpush.common.cluster.IClusterServerInstance;
import com.github.wuzguo.webpush.common.cluster.IClusterSession;
import com.github.wuzguo.webpush.common.enums.MessageTypeEnum;
import com.github.wuzguo.webpush.common.exception.FrameException;
import com.github.wuzguo.webpush.common.util.HttpUtils;
import com.github.wuzguo.webpush.common.vo.BaseMessageVo;
import com.github.wuzguo.webpush.common.vo.GroupMessageVo;
import com.github.wuzguo.webpush.common.vo.ResultVo;
import com.github.wuzguo.webpush.common.vo.SystemMessageVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * http集群消息发送
 *
 * @author wuzguo
 * @date 2016年12月27日 下午4:37:43
 */

//@Component
public class HttpClusterMessageSend implements IClusterMessageSend {

    /**
     * 日志
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(HttpClusterMessageSend.class);


    private static final String HTTP_PROTOCOL_HEADER = "http://";

    @Autowired
    private IClusterServerInstance clusterServerInstance;

    /**
     * 服务实例ID
     **/
    @Value("#{clusterServerInstanceVo.instanceId}")
    private String serverInstanceId;

    @Autowired
    private IClusterSession clusterSession;

    /**
     * @param msgType
     * @param uid
     * @param msgVo
     */
    @Override
    public void p2pMessage(final MessageTypeEnum msgType, final String uid, final BaseMessageVo msgVo) {
        // 如果 clusterServerInstance 为空
        if (clusterServerInstance == null) {
            LOGGER.error("send p2pMessage error, clusterServerInstance is null");
            return;
        }

        Set<String> stringSet = clusterSession.getServerInstanceIds(uid);
        if (stringSet == null || stringSet.size() <= 0) {
            return;
        }
        try {

            for (String instanceId : stringSet) {
                if (serverInstanceId.equals(instanceId)) {
                    continue;
                }

                String host = clusterServerInstance.getServerInstanceHost(instanceId);
                String hostUrl = HTTP_PROTOCOL_HEADER + host + "/sync/message/" + msgType.name() + "/" + uid;
                this.httpPost(msgVo, hostUrl);
            }
        } catch (FrameException e) {
            LOGGER.error("send p2pMessage error, cluster server instance: " + ", msg:" + msgVo.toString(), e);
        } catch (Exception e) {
            LOGGER.error("send p2pMessage error, cluster server instance:" + uid + ", msg:" + msgVo.toString(), e);
        }
    }


    /**
     * @param groupId
     * @param msgVo
     */
    @Override
    public void groupMessage(final String groupId, final GroupMessageVo msgVo) {
        // 如果 clusterServerInstance 为空
        if (clusterServerInstance == null) {
            LOGGER.error("send groupMessage error, clusterServerInstance is null");
            return;
        }

        Map<String, String> stringMap = clusterServerInstance.getServerInstanceIds();
        if (stringMap == null || stringMap.size() <= 0) {
            return;
        }

        try {
            Iterator<String> iter = stringMap.keySet().iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                String host = stringMap.get(key);

                if (serverInstanceId.equals(key)) {
                    continue;
                }

                String hostUrl = HTTP_PROTOCOL_HEADER + host + "/sync/message/group/" + groupId;
                this.httpPost(msgVo, hostUrl);
            }
        } catch (FrameException e) {
            LOGGER.error("send groupMessage error, cluster server instance: " + ", msg:" + msgVo.toString(), e);
        } catch (Exception e) {
            LOGGER.error("send groupMessage error, cluster server instance: " + ", msg:" + msgVo.toString(), e);
        }
    }

    /**
     * @param msgVo
     */
    @Override
    public void systemMessage(final SystemMessageVo msgVo) {
        // 如果 clusterServerInstance 为空
        if (clusterServerInstance == null) {
            LOGGER.error("send systemMessage error, clusterServerInstance is null");
            return;
        }

        String msg = JSON.toJSONString(msgVo);
        Map<String, String> stringMap = clusterServerInstance.getServerInstanceIds();
        if (stringMap == null || stringMap.size() <= 0) {
            return;
        }

        try {
            Iterator<String> iter = stringMap.keySet().iterator();
            while (iter.hasNext()) {

                String instanceId = iter.next();
                String host = stringMap.get(instanceId);

                if (serverInstanceId.equals(instanceId)) {
                    continue;
                }

                String hostUrl = HTTP_PROTOCOL_HEADER + host + "/sync/message/system";
                this.httpPost(msgVo, hostUrl);
            }
        } catch (FrameException e) {
            LOGGER.error("send systemMessage error, cluster server instance: " + ", msg:" + msgVo.toString(), e);
        } catch (Exception e) {
            LOGGER.error("send systemMessage error, cluster server instance: " + ", msg:" + msgVo.toString(), e);
        }
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

        JSONObject resultJson = HttpUtils.post(url, new StringEntity(objectJson.toString(), ContentType.APPLICATION_JSON), 2000)
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
}
