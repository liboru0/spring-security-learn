package com.liboru.springsecurity.learn.securitywebdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liboru.springsecurity.learn.securitywebdemo.model.po.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {

}
