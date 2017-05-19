package com.github.wuzguo.server.action;

/**
 * 动作抽象类
 *
 * @author wuzguo
 * @date 2016年12月18日 下午3:09:22
 */
public abstract class AbstractActionDataListener<T> implements ActionDataListener<T> {
    /**
     * 动作名称
     */
    protected String actionName;

}
