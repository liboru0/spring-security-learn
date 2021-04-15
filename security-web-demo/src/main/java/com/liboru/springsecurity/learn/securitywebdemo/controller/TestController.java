package com.liboru.springsecurity.learn.securitywebdemo.controller;

import com.liboru.springsecurity.learn.securitywebdemo.model.vo.UserVo;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {

    @GetMapping("test01")
    public String test01() {
        return "test01 hello";
    }

    @GetMapping("test02")
    public String test02() {
        return "test02 hello";
    }

    @GetMapping("test03")
    public String test03() {
        return "test03 hello";
    }

    @GetMapping("test04")
    public String test04() {
        return "test04 hello";
    }

    @Secured({"ROLE_sale2", "ROLE_admin"})
    @GetMapping("test05")
    public String test05() {
        return "test05 hello";
    }

    @PreAuthorize("hasAnyAuthority('normal','admin')")
    @GetMapping("test06")
    public String test06() {
        return "test06 hello";
    }

    @PostAuthorize("hasAnyAuthority('normal','admin')")
    @GetMapping("test07")
    public String test07() {
        return "test07 hello";
    }

    /**
     * 有 normal或admin 权限的用户可以登录
     * 返回值中只有 username!='zhangsan' 的可以返回
     */
    @GetMapping("test08")
    @PreAuthorize("hasAnyAuthority('normal','admin')")
    @PostFilter("filterObject.username != 'zhangsan'")
    public List<UserVo> test08() {
        List<UserVo> users = new ArrayList<>();
        users.add(new UserVo(1L, "zhangsan", "123"));
        users.add(new UserVo(2L, "lisi", "111"));
        return users;
    }

    /**
     * 有 normal或admin 权限的用户可以登录
     * 返回值中只有 username=='zhangsan' 的可以返回
     */
    @PostMapping("test09")
    @PreAuthorize("hasAnyAuthority('normal','admin')")
    @PreFilter("filterObject.username=='zhangsan'")
    public List<UserVo> test09(@RequestBody List<UserVo> users) {
        return users;
    }

}
