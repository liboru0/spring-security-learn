package com.liboru.acl.common.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liboru.acl.common.security.entity.SecurityUser;
import com.liboru.acl.common.security.entity.User;
import com.liboru.acl.common.security.security.TokenManager;
import com.liboru.acl.common.service.base.utils.R;
import com.liboru.acl.common.service.base.utils.ResponseUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * 认证过滤器
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager  authenticationManager;
    private TokenManager  tokenManager;
    private RedisTemplate  redisTemplate;

    public  TokenLoginFilter(AuthenticationManager  authenticationManager,
                             TokenManager tokenManager, RedisTemplate redisTemplate) {
        this. authenticationManager = authenticationManager;
        this. tokenManager = tokenManager;
        this. redisTemplate = redisTemplate;
        this.setPostOnly( false);
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher( "/admin/acl/login", "POST"));
    }

    /**
     * 获取表单提交的用户名/密码
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // 获取表单提交的用户名/密码
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername()
                    ,user.getPassword(), Collections.emptyList()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

    /**
     * 认证成功
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // 认证成功之后的用户信息
        SecurityUser user = (SecurityUser)authResult.getPrincipal();
        String username = user.getUsername();
        // 把用户权限列表放到redis中
        redisTemplate.opsForValue().set(username,user.getAuthorities());
        // 根据用户名生成 token
        String token = tokenManager.createToken(username);
        // 返回 token
        ResponseUtil.out(response, R.ok().data("token",token));
    }

    /**
     * 认证失败
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        ResponseUtil.out(response, R.error());
    }

}
