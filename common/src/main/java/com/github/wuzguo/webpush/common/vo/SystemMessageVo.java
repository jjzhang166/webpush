package com.github.wuzguo.webpush.common.vo;


import com.github.wuzguo.webpush.common.annotation.Comment;

import java.io.Serializable;


/**
 * 系统消息
 *
 * @author wuzguo
 * @date 2016年12月14日 下午5:11:19
 */
@Comment("系统(广播)消息对象")
public class SystemMessageVo extends BaseMessageVo implements Serializable {
    /**
     * 所属系统
     **/
    @Comment(value = "所属系统")
    protected String sys;

    public String getSys() {
        return sys;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }

    @Override
    public String toString() {
        return "SystemMessageVo [sys=" + sys + ", to=" + to + ", time=" + time + ", text=" + text + "]";
    }

}
