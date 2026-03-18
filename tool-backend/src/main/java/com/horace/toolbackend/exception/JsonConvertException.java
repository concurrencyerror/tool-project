package com.horace.toolbackend.exception;

/**
 * Exception class for JSON serialization and deserialization errors.
 */
public class JsonConvertException extends RuntimeException {

    public JsonConvertException(String message, Throwable cause) {
        super(message, cause);
    }
}
