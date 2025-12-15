package com.horace.toolbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 安全相关的配置
 */
@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain springSecurityFilterChain(HttpSecurity http) {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // 1) 放行静态资源
                        .requestMatchers("/favicon.ico", "/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                        // 2) 放行登录页/健康检查/公开接口
                        .requestMatchers("/login", "/api/public/**").permitAll()
                        // 3) 权限控制
                        .requestMatchers("/api/**").authenticated()// 需要登录
                        .anyRequest().denyAll() // 或 .authenticated()
                );
        return http.build();
    }
}
