package com.example.namustudy.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter { // 상속 받으면 손 쉽게 설정 가능

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        // 특정 요청은 시큐리티 체크 안하도록 설정
        http.authorizeRequests()
                .mvcMatchers("/","/login","/sign-up","/check-email","/check-email-token","/email-login","/check-email-login","/login-link")
                .permitAll().mvcMatchers(HttpMethod.GET,"/profile/*").permitAll().anyRequest().authenticated();
    }

    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
