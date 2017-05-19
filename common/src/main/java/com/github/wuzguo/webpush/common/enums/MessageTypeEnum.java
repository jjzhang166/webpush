package com.github.wuzguo.webpush.common.enums;


import com.github.wuzguo.webpush.common.vo.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息类型
 *
 * @author wuzguo
 * @date 2016年12月15日 下午12:44:27
 */
public enum MessageTypeEnum {
    p2p("点对点消息", P2PMessageVo.class),
    bizp2p("业务点对点消息", BusinessP2PMessageVo.class),
    sysp2p("系统点对点消息", SystemP2PMessageVo.class),
    group("群组消息", GroupMessageVo.class),
    sys("系统消息", SystemMessageVo.class);

    /**
     * class map
     **/
    private static Map<Class, MessageTypeEnum> classMap = new HashMap();

    static {
        for (MessageTypeEnum row : MessageTypeEnum.values()) {
            classMap.put(row.getMessageClass(), row);
        }
    }

    /**
     * 消息名称
     **/
    private String messageName;
    /**
     * 消息class
     **/
    private Class messageClass;

    MessageTypeEnum(String messageName, Class messageClass) {
        this.messageName = messageName;
        this.messageClass = messageClass;
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public Class getMessageClass() {
        return messageClass;
    }

    public void setMessageClass(Class messageClass) {
        this.messageClass = messageClass;
    }

    /**
     * 根据消息对象获取类型枚举
     *
     * @param clazz
     * @return
     */
    public static MessageTypeEnum getMessageTypeEnum(Class<? extends BaseMessageVo> clazz) {
        return classMap.get(clazz);
    }
}
