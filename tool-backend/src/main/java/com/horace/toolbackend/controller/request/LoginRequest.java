package com.horace.toolbackend.controller.request;

public record LoginRequest(
        String username,
        String email,
        String password
) {

    public String loginId() {
        if (username != null && !username.isBlank()) {
            return username.trim();
        }
        if (email != null && !email.isBlank()) {
            return email.trim();
        }
        return "";
    }
}
