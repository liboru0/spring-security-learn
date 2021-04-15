package com.liboru.springsecurity.learn.securitywebdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/user/login")
    public String login(){
        return "login.html";
    }

}
