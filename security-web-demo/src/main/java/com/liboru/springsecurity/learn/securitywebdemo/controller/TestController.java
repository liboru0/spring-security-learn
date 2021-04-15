package com.liboru.springsecurity.learn.securitywebdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("test01")
    public String test01(){
        return "test01 hello";
    }

    @GetMapping("test02")
    public String test02(){
        return "test02 hello";
    }

    @GetMapping("test03")
    public String test03(){
        return "test03 hello";
    }

    @GetMapping("test04")
    public String test04(){
        return "test04 hello";
    }


}
