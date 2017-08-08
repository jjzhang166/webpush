package com.github.wuzguo.webpush.common.vo;


import com.github.wuzguo.webpush.common.annotation.Comment;

/**
 * jwt 令牌vo
 *
 * @author wuzguo
 * @date 2016年12月16日 下午5:42:33
 */
@Comment("JWT令牌对象")
public class JwtTokenVo {
    /**
     * 令牌
     **/
    @Comment(value = "令牌", required = true)
    private String token;
    /**
     * 是否刷新
     **/
    @Comment(value = "是否刷新了令牌")
    private boolean refresh;
    /**
     * 剩余超时时间
     **/
    @Comment(value = "令牌超时剩余时间,单位:秒")
    private long lastExpires;

    public JwtTokenVo() {
    }

    public JwtTokenVo(String token, long lastExpires) {
        super();
        this.token = token;
        this.lastExpires = lastExpires;
    }

    public JwtTokenVo(String token, boolean refresh, long lastExpires) {
        super();
        this.token = token;
        this.refresh = refresh;
        this.lastExpires = lastExpires;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public long getLastExpires() {
        return lastExpires;
    }

    public void setLastExpires(long lastExpires) {
        this.lastExpires = lastExpires;
    }

    @Override
    public String toString() {
        return "JwtTokenVo{" +
                "token='" + token + '\'' +
                ", refresh=" + refresh +
                ", lastExpires=" + lastExpires +
                '}';
    }
}
