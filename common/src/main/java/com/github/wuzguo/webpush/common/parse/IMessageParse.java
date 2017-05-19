package com.github.wuzguo.webpush.common.parse;


import com.github.wuzguo.webpush.common.vo.BaseMessageVo;

/**
 * 消息解析解耦
 *
 * @author wuzguo
 * @date 2016年12月14日 下午5:15:57
 */
public interface IMessageParse<T extends BaseMessageVo, MTC> {

    /**
     * 解析消息
     *
     * @param msg
     * @return
     */
    T parse(MTC mtc);

}
