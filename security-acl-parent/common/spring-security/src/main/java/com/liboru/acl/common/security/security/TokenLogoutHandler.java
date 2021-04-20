package com.liboru.acl.common.security.security;

import com.liboru.acl.common.service.base.utils.R;
import com.liboru.acl.common.service.base.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 退出处理器
 */
@Component
public class TokenLogoutHandler implements LogoutHandler {

    TokenManager tokenManager;

    RedisTemplate redisTemplate;

    @Autowired
    public TokenLogoutHandler(TokenManager tokenManager, RedisTemplate redisTemplate) {
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 从 header 中获取 token
        String token = request.getHeader("token");
        // token 不为空，移除token,从redis中删除token
        if (token != null) {
            tokenManager.removeToken(token);
            String username = tokenManager.getUserInfoFromToken(token);
            redisTemplate.delete(username);
        }
        ResponseUtil.out(response, R.ok());
    }
}
