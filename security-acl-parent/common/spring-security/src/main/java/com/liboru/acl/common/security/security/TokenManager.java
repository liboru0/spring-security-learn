package com.liboru.acl.common.security.security;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * token操作工具类
 */
@Component
public class TokenManager {

    /**
     * token 有效时长
     */
    private long expirationTime = 60 * 60 * 1000;

    /**
     * 编码密钥
     */
    private String tokenSignKey = "123456";

    /**
     * 根据用户名生成 token
     */
    public String createToken(String username) {
        return Jwts.builder().setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP).compact();
    }

    /**
     * 根据 token 得到用户信息
     */
    public String getUserInfoFromToken(String token) {
        return Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token)
                .getBody().getSubject();
    }

    /**
     * 移除token
     * 不需要移除，保留方法
     */
    public void removeToken(String token){}

}
