package com.github.wuzguo.webpush.common.vo;


import com.github.wuzguo.webpush.common.annotation.Comment;

import java.io.Serializable;

/**
 * 群组消息
 *
 * @author wuzguo
 * @date 2016年12月14日 下午5:12:21
 */
@Comment("群组消息对象")
public class GroupMessageVo extends BaseMessageVo implements Serializable {
    /**
     * 所属群组
     **/
    @Comment(value = "群组ID")
    protected String group;
    /**
     * 消息发送人
     **/
    @Comment(value = "消息发送人")
    protected String from;

    public GroupMessageVo() {
    }

    public GroupMessageVo(String to, String group, String from) {
        super(to);
        this.group = group;
        this.from = from;
    }

    public GroupMessageVo(String to, long time, String text, String group, String from) {
        super(to, time, text);
        this.group = group;
        this.from = from;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public String toString() {
        return "GroupMessageVo [group=" + group + ", from=" + from + ", to=" + to + ", time=" + time + ", text=" + text
                + "]";
    }

}
