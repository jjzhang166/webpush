package com.github.wuzguo.server.cluster.config;


import com.github.wuzguo.server.cluster.server.ConfigClusterServerInstance;
import com.github.wuzguo.server.cluster.server.ZooKeeperClusterServerInstance;
import com.github.wuzguo.webpush.common.cluster.IClusterServerInstance;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * 集群服务实例配置
 *
 * @author wuzguo
 * @date 2016年12月26日 下午3:19:07
 */
@Configuration
@ConfigurationProperties(prefix = "cluster.server")
public class ClusterServerInstanceConfig {
    /**
     * 日志
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ClusterServerInstanceConfig.class);
    /**
     * 集群模型
     **/
    private String model;

    /**
     * 实例主机ID对应的关系集合
     */
    private String instancesHosts;

    /**
     * 实例主机ID对应的主机
     */
    private String instanceHost;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getInstanceHost() {
        return instanceHost;
    }

    public void setInstanceHost(String instanceHost) {
        this.instanceHost = instanceHost;
    }

    public String getInstancesHosts() {
        return instancesHosts;
    }

    public void setInstancesHosts(String instancesHosts) {
        this.instancesHosts = instancesHosts;
    }

    @Bean
    public IClusterServerInstance configClusterServerInstance() {
        LOGGER.debug("add cluster server instance, model:{}, instancesHosts:{}", model, instancesHosts);
        IClusterServerInstance clusterServerInstance = null;
        if (StringUtils.equals("config", model)) {
            clusterServerInstance = new ConfigClusterServerInstance();
            clusterServerInstance.install();
            for (String instances : instancesHosts.split(";")) {
                if (StringUtils.isNotEmpty(instances)) {
                    String[] hosts = instances.split(",");
                    if (StringUtils.isNotEmpty(hosts[0]) && StringUtils.isNotEmpty(hosts[1])) {
                        clusterServerInstance.start(hosts[0], hosts[1]);
                    }
                }
            }
        } else if (StringUtils.equals("zookeeper", model)) {
            clusterServerInstance = new ZooKeeperClusterServerInstance();
            clusterServerInstance.install();
        }
        return clusterServerInstance;
    }

    /**
     * 这里必须加上 name 属性，否则报错：
     * SpelEvaluationException: EL1008E: Property or field 'clusterServerInstanceVo'
     * cannot be found on object of type 'org.springframework.beans.factory.config.BeanExpressionContext'
     *
     * @return clusterServerInstanceVo
     */
    @Bean(name = "clusterServerInstanceVo")
    public ClusterServerInstanceVo configClusterServerInstanceVo() {

        if (StringUtils.isEmpty(instanceHost)) {
            instanceHost = "01,localhost:9000";
            LOGGER.debug("cluster server instance id is null, set instanceHost {}.}", instanceHost);
        }

        ClusterServerInstanceVo clusterServerInstanceVo = new ClusterServerInstanceVo();
        String[] hosts = instanceHost.split(",");
        if (StringUtils.isEmpty(hosts[0])) {
            hosts[0] = "01";
        }

        if (StringUtils.isEmpty(hosts[1])) {
            hosts[0] = "localhost:9000";
        }

        clusterServerInstanceVo.setInstanceId(StringUtils.trim(hosts[0]));
        clusterServerInstanceVo.setInstanceHost(hosts[1]);

        LOGGER.debug("cluster server instance id {}, instanceHost host {}", hosts[0], hosts[1]);
        return clusterServerInstanceVo;
    }
}
