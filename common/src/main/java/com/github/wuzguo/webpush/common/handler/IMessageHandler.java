package com.github.wuzguo.webpush.common.handler;


import com.github.wuzguo.webpush.common.vo.BaseMessageVo;

import javax.websocket.Session;

/**
 * 消息处理
 *
 * @author wuzguo
 * @date 2016年12月14日 下午6:03:05
 */
public interface IMessageHandler<T extends BaseMessageVo> {

    /**
     * 消息处理名称
     *
     * @return
     */
    String[] handleNames();

    /**
     * 处理
     *
     * @param msg
     * @param session
     */
    void handle(T msg, Session session);
}
