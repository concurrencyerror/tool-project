package com.horace.toolbackend.controller;

import com.horace.toolbackend.controller.request.LoginRequest;
import com.horace.toolbackend.controller.response.ApiSuccessResponse;
import com.horace.toolbackend.controller.response.AuthUserResponse;
import com.horace.toolbackend.entity.Tenant;
import com.horace.toolbackend.entity.User;
import com.horace.toolbackend.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void loginStoresSecurityContextInSession() {
        AuthController authController = new AuthController(authenticationManager, userService);
        MockHttpServletRequest request = new MockHttpServletRequest();

        User user = buildUser();
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(
                user.getUsername(),
                null,
                AuthorityUtils.createAuthorityList("ROLE_USER")
        );

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(userService.findByLoginId("admin@example.com")).thenReturn(Optional.of(user));

        ApiSuccessResponse<AuthUserResponse> response = authController.login(
                new LoginRequest(null, "admin@example.com", "password"),
                request
        );

        assertEquals(200, response.code());
        assertEquals("登录成功", response.message());
        assertEquals("admin@example.com", response.data().username());

        HttpSession session = request.getSession(false);
        assertNotNull(session);
        assertNotNull(session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY));
    }

    @Test
    void logoutClearsSession() {
        AuthController authController = new AuthController(authenticationManager, userService);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.getSession(true).setAttribute("demo", "value");
        SecurityContextHolder.getContext().setAuthentication(
                UsernamePasswordAuthenticationToken.authenticated(
                        "admin@example.com",
                        null,
                        AuthorityUtils.createAuthorityList("ROLE_USER")
                )
        );

        ApiSuccessResponse<Void> result = authController.logout(request, response);

        assertEquals(200, result.code());
        assertEquals("退出成功", result.message());
        assertNull(request.getSession(false));
    }

    private User buildUser() {
        Tenant tenant = new Tenant();
        tenant.setId(99L);
        tenant.setName("demo-tenant");

        User user = new User();
        user.setId(1L);
        user.setUsername("admin@example.com");
        user.setPassword("$2a$10$demo");
        user.setTenant(tenant);
        return user;
    }
}
