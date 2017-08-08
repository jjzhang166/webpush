package com.github.wuzguo.webpush.common.vo;


import com.github.wuzguo.webpush.common.annotation.Comment;

import java.io.Serializable;

/**
 * 消息对象
 *
 * @author wuzguo
 * @date 2016年12月14日 下午5:02:27
 */
@Comment("基本消息对象")
public class BaseMessageVo implements Serializable {
    /**
     * 消息接收人
     **/
    @Comment(value = "消息接收人")
    protected String to;
    /**
     * 消息时间
     **/
    @Comment(value = "消息时间")
    protected long time;
    /**
     * 消息内容
     **/
    @Comment(value = "消息内容", required = true)
    protected String text;

    public BaseMessageVo() {
    }

    public BaseMessageVo(String to) {
        this.to = to;
    }

    public BaseMessageVo(String to, long time, String text) {
        this.to = to;
        this.time = time;
        this.text = text;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "BaseMessageVo [to=" + to + ", time=" + time + ", text=" + text + "]";
    }

}
