package com.horace.toolbackend.exception;

import java.time.Instant;

/**
 * Unified error response for REST endpoints.
 *
 * @param timestamp request failure time
 * @param status    HTTP status code
 * @param error     HTTP status text
 * @param message   readable error message
 * @param path      request path
 */
public record ApiErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
