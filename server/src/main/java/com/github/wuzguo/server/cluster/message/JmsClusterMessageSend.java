package com.github.wuzguo.server.cluster.message;

import com.alibaba.fastjson.JSON;
import com.github.wuzguo.webpush.common.cluster.IClusterMessageSend;
import com.github.wuzguo.webpush.common.cluster.IClusterServerInstance;
import com.github.wuzguo.webpush.common.cluster.IClusterSession;
import com.github.wuzguo.webpush.common.enums.MessageTypeEnum;
import com.github.wuzguo.webpush.common.vo.BaseMessageVo;
import com.github.wuzguo.webpush.common.vo.GroupMessageVo;
import com.github.wuzguo.webpush.common.vo.SystemMessageVo;
import org.apache.activemq.command.ActiveMQTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * jms集群消息发送
 *
 * @author wuzguo
 * @date 2016年12月19日 下午7:35:40
 */

//@Component
public class JmsClusterMessageSend implements IClusterMessageSend {

    /**
     * 日志
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(JmsClusterMessageSend.class);

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    /**
     * p2p消息队列前缀，默认：MPS. CJQMPF.
     **/
    @Value("${cluster.jms.queue.message.p2p.firstName:MPS.CJQMPFN.}")
    private String p2pMessageQueueFirstName;

    /**
     * 群组消息队列前缀，默认： MPS.CJQMPF.
     **/
    @Value("${cluster.jms.queue.message.group.firstName:MPS.CJQMGFN.}")
    private String groupMessageQueueFirstName;

    /**
     * 系统消息队列名称，默认： MPS.CJTMS.
     **/
    @Autowired
    private ActiveMQTopic sytemMessageTopic;

    @Autowired
    private IClusterServerInstance clusterServerInstance;

    @Autowired
    private IClusterSession clusterSession;

    /**
     * 服务实例ID
     **/
    @Value("#{clusterServerInstanceVo.instanceId}")
    private String serverInstanceId;

    @Override
    public void p2pMessage(final MessageTypeEnum msgType, final String uid, final BaseMessageVo msgVo) {
        // 如果 clusterServerInstance 为空
        if (clusterServerInstance == null) {
            LOGGER.error("send systemMessage error, clusterServerInstance is null");
            return;
        }

        Set<String> stringSet = clusterSession.getServerInstanceIds(uid);
        if (stringSet == null || stringSet.size() <= 0) {
            return;
        }

        for (String instanceId : stringSet) {
            if (serverInstanceId.equals(instanceId)) {
                continue;
            }
            jmsMessagingTemplate.convertAndSend(p2pMessageQueueFirstName + instanceId, JSON.toJSONString(msgVo), new HashMap<String, Object>() {{
                put("uid", uid);
                put("msgType", msgType.name());
            }});
        }
    }

    @Override
    public void groupMessage(final String groupId, final GroupMessageVo msgVo) {
        // 如果 clusterServerInstance 为空
        if (clusterServerInstance == null) {
            LOGGER.error("send systemMessage error, clusterServerInstance is null");
            return;
        }

        Map<String, String> stringMap = clusterServerInstance.getServerInstanceIds();
        if (stringMap == null || stringMap.size() <= 0) {
            return;
        }

        Iterator<String> iter = stringMap.keySet().iterator();

        while (iter.hasNext()) {

            String instanceId = iter.next();

            String host = stringMap.get(instanceId);

            if (serverInstanceId.equals(instanceId)) {
                continue;
            }

            jmsMessagingTemplate.convertAndSend(groupMessageQueueFirstName + instanceId, JSON.toJSONString(msgVo), new HashMap<String, Object>() {{
                put("gid", groupId);
            }});
        }
    }

    @Override
    public void systemMessage(final SystemMessageVo msgVo) {
        jmsMessagingTemplate.convertAndSend(sytemMessageTopic, JSON.toJSONString(msgVo));
    }

}
