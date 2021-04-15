package com.liboru.springsecurity.learn.securitywebdemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index")
public class IndexController {

    @RequestMapping("")
    public String index(){
        return "index pages";
    }

}
