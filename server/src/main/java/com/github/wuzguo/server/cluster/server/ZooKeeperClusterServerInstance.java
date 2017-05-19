package com.github.wuzguo.server.cluster.server;


import com.github.wuzguo.webpush.common.cluster.IClusterServerInstance;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

/**
 * ZooKeeper服务集群
 *
 * @author wuzguo
 * @date 2016年12月19日 下午7:34:50
 */
//@Component
public class ZooKeeperClusterServerInstance implements IClusterServerInstance {

    /**
     * 日志
     */
    protected static final Logger logger = LoggerFactory.getLogger(ZooKeeperClusterServerInstance.class);


    private Map<String, String> serverInstanceIds = new HashMap<String, String>();

    /**
     * zk连接地址
     **/
    @Value("/${cluster.server.zk.connections:null}")
    private String zkConnections;
    /**
     * zk连接超时，默认：10000， 单位：毫秒
     **/
    @Value("${cluster.server.zk.connectionTimeout:10000}")
    private int zkConnectionTimeout;
    /**
     * zk监听主节点
     **/
    @Value("${cluster.server.zk.mainNode:MPS.ZK.MAIN}")
    private String mainNodeName;
    /**
     * 集群服务实例ID，将注册为子节点
     **/
    @Value("/#{clusterServerInstanceVo.instanceId}")
    private String nodeName;

    @Override
    public void install() {
        logger.info("cluster server install zookeeper connection:{}, connection time:{}.", zkConnections, zkConnectionTimeout);
        ZkClient zkClient = new ZkClient(zkConnections, zkConnectionTimeout);

        //主节点不在创建永久节点
        if (!zkClient.exists(mainNodeName)) {
            logger.info("cluster server zookeeper main node not exists, create persistent main node, node name:{}. ", mainNodeName);
            zkClient.createPersistent(mainNodeName);
        }

        //监听并刷新列表
        zkClient.subscribeChildChanges(mainNodeName, new IZkChildListener() {
            @Override
            public void handleChildChange(final String parentPath, final List<String> currentChilds) throws Exception {
                logger.info("cluster server zookeeper child change replace server instance id set, old ids:{}, new ids:{}.", serverInstanceIds, currentChilds);

                serverInstanceIds = new HashMap<String, String>(currentChilds.size() * 3 / 2 + 1);
                for (String child : currentChilds) {
                    String[] strings = child.split(",");
                    if (StringUtils.isNotEmpty(strings[0]) && StringUtils.isNotEmpty(strings[1])) {
                        serverInstanceIds.put(strings[0], strings[1]);
                    }
                }
            }
        });

        //未配置子节点，创建序列临时节点
        if (StringUtils.isEmpty(nodeName)) {
            logger.error("cluster server zookeeper, server instance is null, create child server instance ephemeral sequential node {}。", nodeName);
            nodeName = zkClient.createEphemeralSequential(nodeName, null);
        } else {
            //子节点不允许重复
            if (zkClient.exists(nodeName)) {
                logger.error("cluster server zookeeper child server instance node {} is exists, server exit。", nodeName);
                System.exit(0);
            }

            logger.info("cluster server zookeeper create child server instance ephemeral node {}。", nodeName);
            //创建临时节点
            zkClient.createEphemeral(nodeName);
        }
    }

    @Override
    public void start(final String serverInstanceId, final String serverInstanceHost) {
        serverInstanceIds.put(serverInstanceId, serverInstanceHost);
    }

    @Override
    public void disconnect(final String serverInstanceId) {
        serverInstanceIds.remove(serverInstanceId);
    }

    @Override
    public Map<String, String> getServerInstanceIds() {
        return serverInstanceIds;
    }

    @Override
    public String getServerInstanceHost(String serverInstanceId) {
        return serverInstanceIds.get(serverInstanceId);
    }

    @Override
    public String toString() {
        return "ZooKeeperClusterServerInstance{" +
                "serverInstanceIds=" + serverInstanceIds +
                ", zkConnections='" + zkConnections + '\'' +
                ", zkConnectionTimeout=" + zkConnectionTimeout +
                ", mainNodeName='" + mainNodeName + '\'' +
                ", nodeName='" + nodeName + '\'' +
                '}';
    }
}
