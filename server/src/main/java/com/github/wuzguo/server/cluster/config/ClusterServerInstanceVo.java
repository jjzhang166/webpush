package com.github.wuzguo.server.cluster.config;

/**
 * 集群服务当前实例VO
 *
 * @author wuzguo
 * @date 2016年12月22日 下午12:39:30
 */
public class ClusterServerInstanceVo {
    // 当前实例ID
    private String instanceId;
    // 当前实例的主机：IP（域名) + 端口号
    private String instanceHost;

    public ClusterServerInstanceVo() {
    }

    public ClusterServerInstanceVo(String instanceId, String instanceHost) {
        this.instanceId = instanceId;
        this.instanceHost = instanceHost;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceHost() {
        return instanceHost;
    }

    public void setInstanceHost(String instanceHost) {
        this.instanceHost = instanceHost;
    }

    @Override
    public String toString() {
        return "ClusterServerInstanceVo{" +
                "instanceId='" + instanceId + '\'' +
                ", instanceHost='" + instanceHost + '\'' +
                '}';
    }
}
