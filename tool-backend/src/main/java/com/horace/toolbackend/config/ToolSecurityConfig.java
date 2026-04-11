package com.horace.toolbackend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.horace.toolbackend.exception.ApiErrorResponse;
import com.horace.toolbackend.security.ToolUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

/**
 * 安全相关的配置
 */
@Configuration
@EnableWebSecurity
public class ToolSecurityConfig {

    private final ObjectMapper objectMapper;
    private final ToolUserDetailsService toolUserDetailsService;

    public ToolSecurityConfig(ObjectMapper objectMapper, ToolUserDetailsService toolUserDetailsService) {
        this.objectMapper = objectMapper;
        this.toolUserDetailsService = toolUserDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(BCryptPasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(toolUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        // 1) 放行静态资源
                        .requestMatchers("/favicon.ico", "/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                        // 2) 放行登录页/健康检查/公开接口
                        .requestMatchers("/api/public/**").permitAll()
                        // 3) 权限控制
                        .requestMatchers("/api/**").authenticated()// 需要登录
                        .anyRequest().denyAll() // 或 .authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) ->
                                writeApiError(response, HttpStatus.UNAUTHORIZED, "未登录或登录已失效", request.getRequestURI()))
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                writeApiError(response, HttpStatus.FORBIDDEN, "没有访问权限", request.getRequestURI()))
                );
        return http.build();
    }

    private void writeApiError(HttpServletResponse response,
                               HttpStatus status,
                               String message,
                               String path) throws IOException {
        response.setStatus(status.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        ));
    }
}
