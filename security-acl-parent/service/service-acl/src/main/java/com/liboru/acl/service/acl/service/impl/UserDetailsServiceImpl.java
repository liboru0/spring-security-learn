package com.liboru.acl.service.acl.service.impl;

import com.liboru.acl.common.security.entity.SecurityUser;
import com.liboru.acl.service.acl.entity.User;
import com.liboru.acl.service.acl.service.PermissionService;
import com.liboru.acl.service.acl.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserService userService;

    @Autowired
    PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 查询用户信息
        User user = userService.selectByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException("用户不存在");
        }

        com.liboru.acl.common.security.entity.User curUser = new com.liboru.acl.common.security.entity.User();
        BeanUtils.copyProperties(user,curUser);

        // 查询用户权限
        List<String> permissionValues = permissionService.selectPermissionValueByUserId(user.getId());

        SecurityUser securityUser = new SecurityUser(curUser);
        securityUser.setPermissionValueList(permissionValues);
        return securityUser;

    }
}
