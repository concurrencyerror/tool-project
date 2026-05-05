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
import org.springframework.web.bind.annotation.*;

/**
 * 登录、退出登录和当前登录用户查询接口。
 */
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    /**
     * 用户登录接口。
     *
     * <p>登录成功后会把 Spring Security 的认证信息写入服务端 session，
     * 浏览器后续请求会通过 JSESSIONID cookie 继续保持登录态。</p>
     *
     * @param request            登录账号和密码
     * @param httpServletRequest 当前 HTTP 请求，用于创建并写入 session
     * @return 当前登录用户的基础信息
     */
    @PostMapping("/public/auth/login")
    public ApiSuccessResponse<AuthUserResponse> login(@RequestBody LoginRequest request,
                                                      HttpServletRequest httpServletRequest) {
        String username = request.username() == null ? "" : request.username().trim();
        if (username.isBlank()) {
            throw new IllegalArgumentException("登录账号不能为空");
        }
        if (request.password() == null || request.password().isBlank()) {
            throw new IllegalArgumentException("密码不能为空");
        }

        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(username, request.password())
        );

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        return ApiSuccessResponse.success("登录成功", AuthUserResponse.from(user));
    }

    /**
     * 用户退出登录接口。
     *
     * <p>退出时会清理当前 Spring Security 上下文，并让当前 session 失效。</p>
     *
     * @param request 当前 HTTP 请求
     * @param response 当前 HTTP 响应
     * @return 空数据成功响应
     */
    @PostMapping("/auth/logout")
    public ApiSuccessResponse<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        new SecurityContextLogoutHandler().logout(request, response, authentication);
        return ApiSuccessResponse.success("退出成功", null);
    }

    /**
     * 查询当前登录用户接口。
     *
     * <p>前端刷新页面时可以调用该接口恢复登录态；如果 session 已过期或用户未登录，
     * Spring Security 会拦截并返回未认证响应。</p>
     *
     * @param authentication 当前认证信息，由 Spring Security 自动注入
     * @return 当前登录用户的基础信息
     */
    @GetMapping("/auth/me")
    public ApiSuccessResponse<AuthUserResponse> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("Not logged in");
        }

        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User does not exist"));

        return ApiSuccessResponse.success("Query success", AuthUserResponse.from(user));
    }
}
