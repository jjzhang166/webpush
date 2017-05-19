package com.github.wuzguo.webpush.common.cluster;


import com.github.wuzguo.webpush.common.enums.MessageTypeEnum;
import com.github.wuzguo.webpush.common.vo.BaseMessageVo;
import com.github.wuzguo.webpush.common.vo.GroupMessageVo;
import com.github.wuzguo.webpush.common.vo.SystemMessageVo;

/**
 * 集群消息同步
 *
 * @author wuzguo
 * @date 2016年12月19日 下午6:56:27
 */
public interface IClusterMessageSync {

    /**
     * 点对点消息同步
     *
     * @param msgType
     * @param uid
     * @param msgVo
     */
    void p2pMessage(MessageTypeEnum msgType, String uid, BaseMessageVo msgVo);

    /**
     * 群组消息同步
     *
     * @param groupId
     * @param vo
     */
    void groupMessage(String groupId, GroupMessageVo msgVo);

    /**
     * 系统消息同步
     *
     * @param msgVo
     */
    void systemMessage(SystemMessageVo msgVo);
}
