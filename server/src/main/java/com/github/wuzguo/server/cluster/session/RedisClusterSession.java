package com.github.wuzguo.server.cluster.session;


import com.github.wuzguo.server.cluster.config.ClusterServerInstanceVo;
import com.github.wuzguo.webpush.common.cluster.IClusterSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 会话redis集群
 *
 * @author wuzguo
 * @date 2016年12月19日 下午7:14:41
 */
@Component
public class RedisClusterSession implements IClusterSession {

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${cluster.session.redis.firstKey:MPS.CSRKF.}")
    /** key前缀，默认： MPS.CSRKF. **/
    private String firstKey;

    /**
     * 超时时间，默认：3600秒,1小时
     **/
    @Value("${cluster.session.redis.timeout:3600}")
    private long timeout;

   /**
     * 服务实例ID
     **/
    @Value("#{clusterServerInstanceVo.instanceId}")
    private String serverInstanceId;


    @Override
    public void open(final String uId) {
        String key = firstKey + uId;
        BoundSetOperations setOperations = redisTemplate.boundSetOps(key);
        setOperations.expire(timeout, TimeUnit.SECONDS);
        long size = setOperations.add(serverInstanceId);
    }

    @Override
    public void close(final String uId) {
        String key = firstKey + uId;
        BoundSetOperations setOperations = redisTemplate.boundSetOps(key);
        setOperations.remove(serverInstanceId);

        long size = setOperations.size();
        //删除为空的会话映射
        if (size == 0) {
            redisTemplate.delete(key);
        }
    }

    @Override
    public Set<String> getServerInstanceIds(final String uId) {
        String key = firstKey + uId;
        return redisTemplate.boundSetOps(key).members();
    }

    @Override
    public boolean exists(final String uId) {
        String key = firstKey + uId;
        BoundSetOperations setOperations = redisTemplate.boundSetOps(key);
        long size = setOperations.size();
        return size > 0;
    }

}
