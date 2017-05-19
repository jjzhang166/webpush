package com.github.wuzguo.server.action;

import com.corundumstudio.socketio.listener.DataListener;

/**
 * 消息动作接口
 *
 * @author wuzguo
 * @date 2016年12月16日 下午12:49:23
 */
public interface ActionDataListener<T> extends DataListener<T> {

    /**
     * 获取动作名称
     *
     * @return
     */
    String getActionName();

    /**
     * 获取数据class类型
     *
     * @return
     */
    Class getDataClass();
}
