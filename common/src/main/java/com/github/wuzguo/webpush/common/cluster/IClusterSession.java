package com.github.wuzguo.webpush.common.cluster;

import java.util.Set;

/**
 * 会话集群接口
 *
 * @author wuzguo
 * @date 2016年12月19日 下午6:44:58
 */
public interface IClusterSession {

    /**
     * 会话打开
     *
     * @param uid
     */
    void open(String uid);

    /**
     * 会话关闭
     *
     * @param uid
     */
    void close(String uid);

    /**
     * 获取用户所在服务实例
     *
     * @param uid
     * @return
     */
    Set<String> getServerInstanceIds(String uid);

    /**
     * 是否登录
     *
     * @param uid
     * @return
     */
    boolean exists(String uid);

}
