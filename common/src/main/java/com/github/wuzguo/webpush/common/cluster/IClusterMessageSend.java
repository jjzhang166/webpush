package com.github.wuzguo.webpush.common.cluster;


import com.github.wuzguo.webpush.common.enums.MessageTypeEnum;
import com.github.wuzguo.webpush.common.vo.BaseMessageVo;
import com.github.wuzguo.webpush.common.vo.GroupMessageVo;
import com.github.wuzguo.webpush.common.vo.SystemMessageVo;

/**
 * 集群消息发送
 *
 * @author wuzguo
 * @date 2016年12月19日 下午6:56:27
 */
public interface IClusterMessageSend {

    /**
     * 发送点对点消息
     *
     * @param msgType
     * @param uid
     * @param msgVo
     */
    void p2pMessage(MessageTypeEnum msgType, String uid, BaseMessageVo msgVo);

    /**
     * 发送群组消息
     *
     * @param groupId
     * @param vo
     */
    void groupMessage(String groupId, GroupMessageVo vo);

    /**
     * 发送系统消息
     *
     * @param msgVo
     */
    void systemMessage(SystemMessageVo msgVo);
}
