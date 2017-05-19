package com.github.wuzguo.server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "netty.socket")
public class NettySocketIoConfig {
    /**
     * 日志
     */
    protected static final Logger logger = LoggerFactory.getLogger(NettySocketIoConfig.class);

    /**
     * 地址
     **/
    private String hostname;
    /**
     * 端口
     **/
    private int port;
    /**
     * 上下文路径
     **/
    private String context;
    /**
     * ssl证书路径
     **/
    private String keyStorePath;
    /**
     * ssl证书密码
     **/
    private String keyStorePassword;

    @Bean
    public com.corundumstudio.socketio.Configuration configuration() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        if (hostname != null && !"".equals(hostname)) {
            config.setHostname(hostname);
        }
        config.setPort(port);
        if (context != null) {
            config.setContext(context);
        }

        if (keyStorePath != null && !"".equals(keyStorePath)) {
            logger.info("netty socket io server init config, keyStorePath:{}, keyStorePassword:{}.", keyStorePath, keyStorePassword);
            config.setKeyStore(NettySocketIoConfig.class.getResourceAsStream(keyStorePath));
            config.setKeyStorePassword(keyStorePassword.toString());
        }


        return config;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    @Override
    public String toString() {
        return "NettySocketIoConfig{" +
                "hostname='" + hostname + '\'' +
                ", port=" + port +
                ", context='" + context + '\'' +
                ", keyStorePath='" + keyStorePath + '\'' +
                ", keyStorePassword='" + keyStorePassword + '\'' +
                '}';
    }
}
