package com.horace.toolbackend.controller.response;

import com.horace.toolbackend.entity.Tenant;
import com.horace.toolbackend.entity.User;

public record AuthUserResponse(
        Long id,
        String username,
        Long tenantId,
        String tenantName
) {

    public static AuthUserResponse from(User user) {
        Tenant tenant = user.getTenant();
        return new AuthUserResponse(
                user.getId(),
                user.getUsername(),
                tenant != null ? tenant.getId() : null,
                tenant != null ? tenant.getName() : null
        );
    }
}
