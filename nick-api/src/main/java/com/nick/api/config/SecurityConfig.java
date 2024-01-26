package com.nick.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter{
//    RESTful技术与CSRF的冲突造成的，CSRF默认支持的方法： GET|HEAD|TRACE|OPTIONS，不支持POST。可以在security在配置中禁用掉它。
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);
        http.csrf().disable();
        //配置不需要登陆验证
        http.authorizeRequests().anyRequest().permitAll().and().logout().permitAll();
    }

}