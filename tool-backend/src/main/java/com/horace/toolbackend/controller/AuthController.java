package com.horace.toolbackend.controller;

import com.horace.toolbackend.controller.request.LoginRequest;
import com.horace.toolbackend.controller.response.ApiSuccessResponse;
import com.horace.toolbackend.controller.response.AuthUserResponse;
import com.horace.toolbackend.entity.User;
import com.horace.toolbackend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/public/auth/login")
    public ApiSuccessResponse<AuthUserResponse> login(@RequestBody LoginRequest request,
                                                      HttpServletRequest httpServletRequest) {
        String loginId = request.loginId();
        if (loginId.isBlank()) {
            throw new IllegalArgumentException("登录账号不能为空");
        }
        if (request.password() == null || request.password().isBlank()) {
            throw new IllegalArgumentException("密码不能为空");
        }

        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(loginId, request.password())
        );

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

        User user = userService.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        return ApiSuccessResponse.success("登录成功", AuthUserResponse.from(user));
    }

    @PostMapping("/auth/logout")
    public ApiSuccessResponse<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        new SecurityContextLogoutHandler().logout(request, response, authentication);
        return ApiSuccessResponse.success("退出成功", null);
    }
}
