package com.horace.toolbackend.exception;

public class HttpRequestException extends RuntimeException {
    private final Integer statusCode;
    private final String responseBody;

    public HttpRequestException(String message) {
        super(message);
        this.statusCode = null;
        this.responseBody = null;
    }

    public HttpRequestException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = null;
        this.responseBody = null;
    }

    public HttpRequestException(String message, Integer statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
