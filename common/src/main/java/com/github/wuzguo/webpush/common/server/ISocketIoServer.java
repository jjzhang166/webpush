package com.github.wuzguo.webpush.common.server;


import com.github.wuzguo.webpush.common.enums.MessageTypeEnum;
import com.github.wuzguo.webpush.common.vo.BaseMessageVo;
import com.github.wuzguo.webpush.common.vo.GroupMessageVo;
import com.github.wuzguo.webpush.common.vo.SystemMessageVo;

import java.util.UUID;

/**
 * socket.io服务
 *
 * @author wuzguo
 * @date 2016年12月19日 下午8:03:56
 */
public interface ISocketIoServer {

    /**
     * 启动
     */
    void run();

    /**
     * 停止
     */
    void stop();

    /**
     * 发送点对点消息
     *
     * @param eventName
     * @param uuid
     * @param msgVo
     */
    void sendEvent(String eventName, UUID uuid, BaseMessageVo msgVo);

    /**
     * 发送点对点消息
     *
     * @param msgType
     * @param uid
     * @param msgVo
     * @param cached  缓存
     * @param sync    消息同步
     * @return
     */
    String sendP2pMessage(MessageTypeEnum msgType, String uid, BaseMessageVo msgVo, boolean cached, boolean sync);

    /**
     * 发送群组消息
     *
     * @param groupId
     * @param msgVo
     * @param sync    消息同步
     * @return
     */
    String sendGroupMessage(String groupId, GroupMessageVo msgVo, boolean sync);

    /**
     * 发送系统消息(广播)
     *
     * @param msgVo
     * @param sync  消息同步
     */
    void sendSystemMessage(SystemMessageVo msgVo, boolean sync);
}
