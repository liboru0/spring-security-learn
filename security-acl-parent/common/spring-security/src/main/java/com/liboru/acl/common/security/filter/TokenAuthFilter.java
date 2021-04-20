package com.liboru.acl.common.security.filter;

import com.liboru.acl.common.security.security.TokenManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.CollectionUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 授权过滤器
 */
public class TokenAuthFilter extends BasicAuthenticationFilter {

    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;

    public TokenAuthFilter(AuthenticationManager authManager, TokenManager tokenManager, RedisTemplate redisTemplate) {
        super(authManager);
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 获取当前认证成功用户权限信息
        UsernamePasswordAuthenticationToken authRequest = getAuthentication(request);
        // 将权限信息放到权限上下文中
        if (authRequest != null) {
            SecurityContextHolder.getContext().setAuthentication(authRequest);
        }
        chain.doFilter(request,response);

    }

    /**
     * 从 redis 中获取权限信息
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("token");
        if(token!=null){
            String username = tokenManager.getUserInfoFromToken(token);
            List<String> permissionVaules = (List<String>) redisTemplate.opsForValue().get(username);
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            if(!CollectionUtils.isEmpty(permissionVaules)){
                authorities = permissionVaules.stream().filter(Objects::nonNull)
                        .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            }
            return new UsernamePasswordAuthenticationToken(username,token,authorities);
        }
        return null;
    }
}
