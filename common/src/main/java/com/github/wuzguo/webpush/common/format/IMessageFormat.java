package com.github.wuzguo.webpush.common.format;


import com.github.wuzguo.webpush.common.vo.BaseMessageVo;

/**
 * 消息格式化接口
 *
 * @author wuzguo
 * @date 2016年12月14日 下午5:16:09
 */
public interface IMessageFormat<T extends BaseMessageVo> {

    /**
     * 格式化消息
     *
     * @param msg
     * @return
     */
    String format(T msg);
}
