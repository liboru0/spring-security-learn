package com.liboru.springsecurity.learn.securitywebdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liboru.springsecurity.learn.securitywebdemo.mapper.UserMapper;
import com.liboru.springsecurity.learn.securitywebdemo.model.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getName, username));
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在!!!");
        }

        // hasRole() 和 hasAnyRole() 会在前边拼上 ROLE_
        String authority = "";
        if("zhangsan".equals(username)){
            authority = "admin,ROLE_sale2";
        }else{
            authority = "normal,ROLE_sale1";
        }

        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
        return new org.springframework.security.core.userdetails.User(username,
                new BCryptPasswordEncoder().encode(user.getPassword()),authorities);

    }

}
