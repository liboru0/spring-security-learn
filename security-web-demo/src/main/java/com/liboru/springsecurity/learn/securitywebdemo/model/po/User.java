package com.liboru.springsecurity.learn.securitywebdemo.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String password;
    private Date createTime;
    private Date updateTime;

}
