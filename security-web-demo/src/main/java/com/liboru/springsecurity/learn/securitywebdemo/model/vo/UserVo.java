package com.liboru.springsecurity.learn.securitywebdemo.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {

    private Long id;
    private String username;
    private String password;

}
