package com.github.wuzguo.server.cluster.server;


import com.github.wuzguo.webpush.common.cluster.IClusterServerInstance;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 手工配置服务集群
 *
 * @author wuzguo
 * @date 2016年12月19日 下午7:34:50
 */

//@Component
public class ConfigClusterServerInstance implements IClusterServerInstance {

    private Map<String, String> serverInstanceIds;

    public ConfigClusterServerInstance() {

    }

    @Override
    public void install() {
        serverInstanceIds = new HashMap<String, String>();
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
    public String getServerInstanceHost(final String serverInstanceId) {
        return serverInstanceIds.get(serverInstanceId);
    }


    @Override
    public String toString() {
        return "ConfigClusterServerInstance{" +
                "serverInstanceIds=" + serverInstanceIds +
                '}';
    }
}
