package com.horace.toolbackend.controller.request;

public record LoginRequest(
        String username,
        String password
) {
}
