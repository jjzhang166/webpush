package com.github.wuzguo.webpush.common.cluster;

import java.util.Map;
import java.util.Set;

/**
 * 集群服务实例接口
 *
 * @author wuzguo
 * @date 2016年12月19日 下午7:27:50
 */
public interface IClusterServerInstance {

    /**
     * 安装
     */
    void install();

    /**
     * 集群服务启动
     *
     * @param serverInstanceId
     */
    void start(String serverInstanceId, String serverInstanceHost);

    /**
     * 集群服务断开
     *
     * @param serverInstanceId
     */
    void disconnect(String serverInstanceId);

    /**
     * 集群服务实例ids
     *
     * @return
     */
    Map<String, String> getServerInstanceIds();


    /**
     * @param serverInstanceId 实例ID
     * @return
     */
    String getServerInstanceHost(String serverInstanceId);
}
