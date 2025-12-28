package com.horace.toolbackend.exception;

/**
 * Exception class for third-party API errors.
 * @author Horace
 */
public class ThirdApiException extends RuntimeException {
    public ThirdApiException(String message) {
        super(message);
    }
}
