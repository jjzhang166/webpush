package com.github.wuzguo.server.authenticate.jwt.util;


import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.github.wuzguo.server.authenticate.jwt.config.JwtConfig;
import com.github.wuzguo.webpush.common.vo.JwtTokenUserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {
    /**
     * 创建Session_Key
     */
    private static final String KEY_SESSION = UUID.randomUUID().toString();

    private static JWTVerifier jwtVerifier;

    @Autowired(required = false)
    private JwtConfig jwtConfig;


    public JwtConfig getJwtConfig() {
        return jwtConfig;
    }

    public void setJwtConfig(final JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }


    public static String create(final JwtTokenUserVo userVo, final JwtConfig jwtConfig) throws IllegalArgumentException, JWTCreationException, UnsupportedEncodingException {
        long curr = System.currentTimeMillis();
        String token = JWT.create()
                .withIssuer(jwtConfig.getIssuer())
                //.withIssuedAt(new Date(curr))
                .withNotBefore(new Date(curr))
                .withExpiresAt(new Date(curr + jwtConfig.getExpires() * 1000))
                .withClaim(jwtConfig.getClaimKey(), JSON.toJSONString(userVo))
                .sign(Algorithm.HMAC256(jwtConfig.getSignKey()));
        return token;
    }

    public String create(final JwtTokenUserVo userVo) throws IllegalArgumentException, JWTCreationException, UnsupportedEncodingException {
        return create(userVo, jwtConfig);
    }

    public static String refresh(final String token, final JwtConfig jwtConfig) throws IllegalArgumentException, JWTCreationException, UnsupportedEncodingException {
        JWT jwt = JWT.decode(token);
        long curr = System.currentTimeMillis();
        long expire = jwt.getExpiresAt().getTime();
        if (expire - curr < jwtConfig.getRefreshTimeout()) {
            return create(JSON.parseObject(jwt.getClaim(jwtConfig.getClaimKey()).asString(), JwtTokenUserVo.class), jwtConfig);
        } else {
            return token;
        }
    }

    public String refresh(final String token) throws IllegalArgumentException, JWTCreationException, UnsupportedEncodingException {
        return refresh(token, jwtConfig);
    }

    public static JWT decode(final String token) {
        return JWT.decode(token);
    }

    public static Date getExpiresAt(final String token) {
        if (StringUtils.isEmpty(token)) {
            return new Date();
        }
        return decode(token).getExpiresAt();
    }

    public static long getExpiresTime(final String token) {
        if (StringUtils.isEmpty(token)) {
            return 0L;
        }
        return decode(token).getExpiresAt().getTime();
    }

    public static long lastExpires(final String token) {
        if (StringUtils.isEmpty(token)) {
            return 0L;
        }
        return decode(token).getExpiresAt().getTime() - System.currentTimeMillis();
    }

    public static JwtTokenUserVo getLoginUser(final JWT jwt, final JwtConfig jwtConfig) {
        String jsonStr = jwt.getClaim(jwtConfig.getClaimKey()).asString();
        return JSON.parseObject(jsonStr, JwtTokenUserVo.class);
    }

    public JwtTokenUserVo getLoginUser(final JWT jwt) {
        String jsonStr = jwt.getClaim(jwtConfig.getClaimKey()).asString();
        return JSON.parseObject(jsonStr, JwtTokenUserVo.class);
    }

    public static JWT verify(final String token, final JwtConfig jwtConfig) throws IllegalArgumentException, UnsupportedEncodingException {
        return (JWT) getJWTVerifier(jwtConfig).verify(token);
    }

    public JWT verify(final String token) throws IllegalArgumentException, UnsupportedEncodingException {
        return (JWT) getJWTVerifier(jwtConfig).verify(token);
    }

    public static JWTVerifier getJWTVerifier(final JwtConfig jwtConfig) throws IllegalArgumentException, UnsupportedEncodingException {
        if (jwtVerifier == null) {
            jwtVerifier = JWT.require(Algorithm.HMAC256(jwtConfig.getSignKey()))
                    .withIssuer(jwtConfig.getIssuer())
                    .acceptLeeway(jwtConfig.getLeeway())
                    .build();
        }
        return jwtVerifier;
    }

}
