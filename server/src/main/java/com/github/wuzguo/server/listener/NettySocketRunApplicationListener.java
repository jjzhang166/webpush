package com.github.wuzguo.server.listener;


import com.github.wuzguo.server.action.ActionDataListener;
import com.github.wuzguo.server.server.NettySocketIoServer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.ArrayList;
import java.util.Map;

/**
 * 启动netty socket server
 *
 * @author wuzguo
 * @date 2016年12月16日 下午2:57:59
 */
public class NettySocketRunApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        NettySocketIoServer server = event.getApplicationContext().getBean(NettySocketIoServer.class);

        Map<String, ActionDataListener> actionDataListeners = event.getApplicationContext().getBeansOfType(ActionDataListener.class);
        server.setActionDataListeners(new ArrayList(actionDataListeners.values()));

        server.run();
    }

}
