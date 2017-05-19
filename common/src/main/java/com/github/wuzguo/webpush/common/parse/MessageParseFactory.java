package com.github.wuzguo.webpush.common.parse;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息解析对象工厂
 *
 * @author wuzguo
 * @date 2016年12月15日 上午11:18:51
 */
public class MessageParseFactory {

    protected static MessageParseFactory messageParseFactory = null;

    /**
     * 消息解析对象map
     **/
    private Map<Character, IMessageParse> parseMap = new HashMap();
    /**
     * 默认消息解析对象
     **/
    private IMessageParse defaultMessageParse;

    /**
     * 注册到消息解析工厂
     *
     * @param key
     * @param msgParse
     */
    public synchronized void register(final Character key, final IMessageParse msgParse) {
        parseMap.put(key, msgParse);
    }

    /**
     * 根据key获取消息解析对象
     *
     * @param key
     * @return
     */
    public IMessageParse get(final Character key) {
        return parseMap.get(key);
    }

    /**
     * 根据key获取消息解析对象，没有则返回默认的对象
     *
     * @param key
     * @return
     */
    public IMessageParse take(final Character key) {
        IMessageParse parse = get(key);
        return parse == null ? defaultMessageParse : parse;
    }

    /**
     * 创建实例
     *
     * @return
     */
    public synchronized static MessageParseFactory createFactory() {
        if (messageParseFactory == null) {
            messageParseFactory = new MessageParseFactory();
        }
        return messageParseFactory;
    }

    /**
     * 获取静态实例
     *
     * @return
     */
    public static MessageParseFactory getInstance() {
        return messageParseFactory;
    }
}
