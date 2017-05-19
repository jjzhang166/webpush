package com.github.wuzguo.server.cluster.message;

import com.alibaba.fastjson.JSON;
import com.github.wuzguo.webpush.common.cluster.IClusterMessageSync;
import com.github.wuzguo.webpush.common.enums.MessageTypeEnum;
import com.github.wuzguo.webpush.common.server.ISocketIoServer;
import com.github.wuzguo.webpush.common.vo.BaseMessageVo;
import com.github.wuzguo.webpush.common.vo.GroupMessageVo;
import com.github.wuzguo.webpush.common.vo.SystemMessageVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;

import javax.jms.JMSException;
import javax.jms.TextMessage;

/**
 * jms集群消息同步
 *
 * @author wuzguo
 * @date 2016年12月19日 下午7:36:28
 */
//@Component
public class JmsClusterMessageSync implements IClusterMessageSync {
    /**
     * 日志
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(JmsClusterMessageSync.class);

    @Autowired
    private ISocketIoServer socketIoServer;

    /**
     * 监听p2p消息
     *
     * @param msgType
     * @param uid
     * @param msgVo
     */
    @JmsListener(destination = "${cluster.jms.queue.message.p2p.firstName:MPS.CJQMPFN.}#{clusterServerInstanceVo.instanceId}")
    public void onP2pMessage(final TextMessage textMessage) {
        try {
            String msgType = textMessage.getStringProperty("msgType");
            String uid = textMessage.getStringProperty("uid");
            MessageTypeEnum mte = MessageTypeEnum.valueOf(msgType);
            BaseMessageVo msgVo = (BaseMessageVo) JSON.parseObject(textMessage.getText(), mte.getMessageClass());
            p2pMessage(MessageTypeEnum.valueOf(msgType), uid, msgVo);
        } catch (Exception e) {
            LOGGER.error("onP2pMessage", e);
        }
    }

    /**
     * 监听群组消息
     *
     * @param msgType
     * @param groupId
     * @param msgVo
     */
    @JmsListener(destination = "${cluster.jms.queue.message.group.firstName:MPS.CJQMGFN.}#{clusterServerInstanceVo.instanceId}")
    public void onGroupMessage(final TextMessage textMessage) {
        try {
            String groupId = textMessage.getStringProperty("groupId");
            GroupMessageVo msgVo = JSON.parseObject(textMessage.getText(), GroupMessageVo.class);
            groupMessage(groupId, msgVo);
        } catch (JMSException e) {
            LOGGER.error("onGroupMessage", e);
        }
    }

    /**
     * 监听系统消息
     *
     * @param msgVo
     */
    @JmsListener(destination = "${cluster.jms.topic.message.system:MPS.CJTMS.}")
    public void onSystemMessage(final String textMessage) {
        SystemMessageVo msgVo = JSON.parseObject(textMessage, SystemMessageVo.class);
        systemMessage(msgVo);
    }

    @Override
    public void p2pMessage(final MessageTypeEnum msgType, final String uid, final BaseMessageVo msgVo) {
        socketIoServer.sendP2pMessage(msgType, uid, msgVo, false, false);
    }

    @Override
    public void groupMessage(final String groupId, final GroupMessageVo msgVo) {
        socketIoServer.sendGroupMessage(groupId, msgVo, false);

    }

    @Override
    public void systemMessage(final SystemMessageVo msgVo) {
        socketIoServer.sendSystemMessage(msgVo, false);
    }

}
