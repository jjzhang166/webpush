package com.github.wuzguo.server.authenticate.jwt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "session.jwt")
public class JwtConfig {
    /**
     * 发布系统
     **/
    private String issuer;
    /**
     * 加密key
     **/
    private String signKey;
    /**
     * 自定义信息保存key
     **/
    private String claimKey = "user";
    /**
     * 会话超时时间,单位：秒，默认30分钟
     **/
    private int expires = 30 * 60;
    /**
     * 超时允许范围
     **/
    private long leeway = 0;
    /**
     * 刷新超时token允许时间 ,单位：秒，默认10分钟
     **/
    private long refreshTimeout = 10 * 60;

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getSignKey() {
        return signKey;
    }

    public void setSignKey(String signKey) {
        this.signKey = signKey;
    }

    public String getClaimKey() {
        return claimKey;
    }

    public void setClaimKey(String claimKey) {
        this.claimKey = claimKey;
    }

    public int getExpires() {
        return expires;
    }

    public void setExpires(int expires) {
        this.expires = expires;
    }

    public long getLeeway() {
        return leeway;
    }

    public void setLeeway(long leeway) {
        this.leeway = leeway;
    }

    public long getRefreshTimeout() {
        return refreshTimeout;
    }

    public void setRefreshTimeout(long refreshTimeout) {
        this.refreshTimeout = refreshTimeout;
    }

    @Override
    public String toString() {
        return "JwtConfig{" +
                "issuer='" + issuer + '\'' +
                ", signKey='" + signKey + '\'' +
                ", claimKey='" + claimKey + '\'' +
                ", expires=" + expires +
                ", leeway=" + leeway +
                ", refreshTimeout=" + refreshTimeout +
                '}';
    }
}
