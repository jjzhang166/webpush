package com.github.wuzguo.webpush.common.vo;


import com.github.wuzguo.codegen.annotation.Comment;

import java.io.Serializable;


/**
 * 点对点系统消息
 *
 * @author wuzguo
 * @date 2016年12月14日 下午5:10:31
 */
@Comment("系统点对点消息对象")
public class SystemP2PMessageVo extends BaseMessageVo implements Serializable {
    /**
     * 消息发送系统
     **/
    @Comment(value = "消息发送系统")
    protected String system;

    public SystemP2PMessageVo() {
    }

    public SystemP2PMessageVo(String to, String system) {
        super(to);
        this.system = system;
    }

    public SystemP2PMessageVo(String to, long time, String text, String system) {
        super(to, time, text);
        this.system = system;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    @Override
    public String toString() {
        return "SystemP2PMessageVo [system=" + system + ", to=" + to + ", time=" + time + ", text=" + text + "]";
    }

}
