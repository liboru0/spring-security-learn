package com.liboru.springsecurity.learn.securitywebdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/login.html") // 登录页
                .loginProcessingUrl("/user/login") // 登录访问路径
                .defaultSuccessUrl("/success.html") // 登录成功之后跳转的路径
                .permitAll()
                .and().authorizeRequests()
                    .antMatchers("/","/hello","/user/login").permitAll() // 设置哪些路径可以直接访问，不需要认证
                    // 用户有admin权限才能访问/test01
                    .antMatchers("/test01").hasAuthority("admin")
                    // 用户有admin或者normal权限能访问/test02
                    .antMatchers("/test02").hasAnyAuthority("admin","normal")
                    // 用户有sale1角色才能访问/test03
                    .antMatchers("/test03").hasRole("sale1")
                    // 用户有sale1或者sale2角色能访问/test04
                    .antMatchers("/test04").hasAnyRole("sale1","sale2");
        // 配置没有权限访问时跳转的自定义页面
        http.exceptionHandling().accessDeniedPage("/unauth.html");
        // 配置登出
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/hello").permitAll();
        // 配置记住我
        http.rememberMe().tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(60);
        http.csrf().disable(); // 关闭csrf防护
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        // jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }

}
