package com.github.wuzguo.server.flicker.cached;


import com.github.wuzguo.webpush.common.filcker.IConnectionFilckerMessageCached;
import com.github.wuzguo.webpush.common.vo.BaseMessageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 连接闪烁缓存 redis缓存
 *
 * @author wuzguo
 * @date 2016年12月19日 上午11:35:33
 */
@Component
public class RedisConnectionFilckerMessageCached implements IConnectionFilckerMessageCached {

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${connection.filcker.message.cached.redis.firstKey:MPS.CFMCRKF.}")
    /** key前缀，默认：MPS.CFMCRKF. **/
    private String firstKey;

    /**
     * 超时时间，默认：30秒
     **/
    @Value("${connection.filcker.message.cached.redis.timeout:30}")
    private long timeout;

    /**
     * 获取最大行数，默认：-1全部
     **/
    @Value("${connection.filcker.message.cached.redis.getMaxRows:-1}")
    private long getMaxRows;

    /**
     * @param uid
     * @param msgVo
     */
    @Override
    public void add(final String uid, final BaseMessageVo msgVo) {
        String key = firstKey + uid;
        BoundListOperations ops = redisTemplate.boundListOps(key);
        ops.expire(timeout, TimeUnit.SECONDS);
        ops.leftPush(msgVo);
    }

    /**
     * @param uid
     * @return
     */
    @Override
    public List<BaseMessageVo> remove(final String uid) {
        String key = firstKey + uid;
        BoundListOperations listOperations = redisTemplate.boundListOps(key);
        List<BaseMessageVo> result = listOperations.range(0, getMaxRows);

        for (BaseMessageVo baseMessage : result) {
            listOperations.remove(0, baseMessage);
        }
        if (getMaxRows < 0) {
            redisTemplate.delete(key);
        }

        return result;
    }

}
