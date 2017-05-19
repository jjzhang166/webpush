
package com.github.wuzguo.webpush.pojo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * TODO url
 *
 * @author wuzguo at 2017/5/11 11:32
 */

@Component
public class SocketApiConfig {

    /**
     * 获取token
     */
    @Value("${server.jwt.token}")
    private String tokenGen;


    /**
     * 刷新Token
     */
    @Value("${server.jwt.refresh}")
    private String refreshToken;

    /**
     * 发送消息
     */
    @Value("${server.jwt.message}")
    private String sendMessage;

    public String getTokenGen() {
        return tokenGen;
    }

    public void setTokenGen(String tokenGen) {
        this.tokenGen = tokenGen;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(String sendMessage) {
        this.sendMessage = sendMessage;
    }

    @Override
    public String toString() {
        return "SocketApiConfig{" +
                "tokenGen='" + tokenGen + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", sendMessage='" + sendMessage + '\'' +
                '}';
    }
}
