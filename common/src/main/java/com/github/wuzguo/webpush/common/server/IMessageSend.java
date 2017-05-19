package com.github.wuzguo.webpush.common.server;

/**
 * 消息发送接口
 *
 * @author wuzguo
 * @date 2016年12月14日 下午6:56:14
 */
public interface IMessageSend {

    /**
     * 发送消息
     *
     * @param id
     * @param message
     * @return 发送是否成功
     */
    boolean sendMessage(String id, String message);

}
