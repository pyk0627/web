package com.keshe.crop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

    /**
     * 只使用 BCrypt 做密码哈希，不启用 Spring Security 的登录拦截链。
     * 登录状态由 HttpSession + AuthFilter 管理，便于课程答辩讲解。
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
