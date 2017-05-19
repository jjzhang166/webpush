package com.github.wuzguo.webpush.common.filcker;


import com.github.wuzguo.webpush.common.vo.BaseMessageVo;

import java.util.List;

/**
 * 连接闪烁缓存接口
 *
 * @author wuzguo
 * @date 2016年12月19日 上午11:28:46
 */
public interface IConnectionFilckerMessageCached {

    /**
     * 添加缓存消息
     *
     * @param uid
     * @param msgVo
     * @return
     */
    void add(String uid, BaseMessageVo msgVo);

    /**
     * 删除缓存消息
     *
     * @param uid
     * @return
     */
    List<BaseMessageVo> remove(String uid);

}
