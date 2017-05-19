package com.github.wuzguo.server.cluster.config;


import com.github.wuzguo.server.cluster.message.HttpClusterMessageSend;
import com.github.wuzguo.server.cluster.message.HttpClusterMessageSync;
import com.github.wuzguo.server.cluster.message.JmsClusterMessageSend;
import com.github.wuzguo.server.cluster.message.JmsClusterMessageSync;
import com.github.wuzguo.webpush.common.cluster.IClusterMessageSend;
import com.github.wuzguo.webpush.common.cluster.IClusterMessageSync;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 集群消息队列配置
 *
 * @author wuzguo
 * @date 2016年12月26日 下午3:18:42
 */
@Configuration
public class ClusterMessageConfig {
    /**
     * 系统消息队列名称，默认： MPS.CJTMS.
     **/
    @Value("${cluster.jms.topic.message.system:MPS.CJTMS.}")
    private String sytemMessageQueueName;

    /**
     * 集群消息通道，默认： jms
     **/
    @Value("${cluster.message.channel:http}")
    private String clusterMessageChannel;

    @Bean
    public ActiveMQTopic sytemMessageTopic() {
        return new ActiveMQTopic(sytemMessageQueueName);
    }

    @Bean
    public IClusterMessageSend clusterMessageSend() {
        if (StringUtils.equals("jms", clusterMessageChannel)) {
            return new JmsClusterMessageSend();
        } else if (StringUtils.equals("http", clusterMessageChannel)) {
            return new HttpClusterMessageSend();
        }

        return null;
    }

    @Bean
    public IClusterMessageSync clusterMessageSync() {
        if (StringUtils.equals("jms", clusterMessageChannel)) {
            return new JmsClusterMessageSync();
        } else if (StringUtils.equals("http", clusterMessageChannel)) {
            //  return new HttpClusterMessageSync();
            return null;
        }

        return null;
    }
}
