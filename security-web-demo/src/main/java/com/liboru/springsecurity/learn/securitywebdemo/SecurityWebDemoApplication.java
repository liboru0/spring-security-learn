package com.liboru.springsecurity.learn.securitywebdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.liboru.springsecurity.learn.securitywebdemo.mapper")
@SpringBootApplication
public class SecurityWebDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityWebDemoApplication.class, args);
    }

}
