package com.github.wuzguo.webpush.common.vo;


import com.github.wuzguo.codegen.annotation.Comment;

import java.io.Serializable;


/**
 * 点对点消息
 *
 * @author wuzguo
 * @date 2016年12月14日 下午5:10:31
 */
@Comment("点对点消息对象")
public class P2PMessageVo extends BaseMessageVo implements Serializable {
    /**
     * 消息发送人
     **/
    @Comment(value = "消息发送人")
    protected String from;

    public P2PMessageVo() {
    }

    public P2PMessageVo(String to, String from) {
        super(to);
        this.from = from;
    }

    public P2PMessageVo(String to, long time, String text, String from) {
        super(to, time, text);
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public String toString() {
        return "P2PMessageVo [from=" + from + ", to=" + to + ", time=" + time + ", text=" + text + "]";
    }

}
