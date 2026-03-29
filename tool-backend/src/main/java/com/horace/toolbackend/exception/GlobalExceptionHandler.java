package com.horace.toolbackend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;

/**
 * Converts uncaught exceptions into a unified REST error response.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ThirdApiException.class)
    public ResponseEntity<ApiErrorResponse> handleThirdApiException(ThirdApiException exception,
                                                                    HttpServletRequest request) {
        log.warn("Third-party API request failed: {}", exception.getMessage(), exception);
        return buildResponse(HttpStatus.BAD_GATEWAY, exception.getMessage(), request);
    }

    @ExceptionHandler(JsonConvertException.class)
    public ResponseEntity<ApiErrorResponse> handleJsonConvertException(JsonConvertException exception,
                                                                       HttpServletRequest request) {
        log.error("JSON conversion failed: {}", exception.getMessage(), exception);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception,
                                                                                  HttpServletRequest request) {
        log.warn("Malformed request body: {}", exception.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Malformed JSON request body", request);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            BindException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class
    })
    public ResponseEntity<ApiErrorResponse> handleBadRequestException(Exception exception,
                                                                      HttpServletRequest request) {
        log.warn("Request validation failed: {}", exception.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, resolveBadRequestMessage(exception), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception exception,
                                                            HttpServletRequest request) {
        log.error("Unexpected request failure", exception);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request);
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status,
                                                           String message,
                                                           HttpServletRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(response);
    }

    private String resolveBadRequestMessage(Exception exception) {
        if (exception instanceof MethodArgumentNotValidException methodArgumentNotValidException
                && !methodArgumentNotValidException.getBindingResult().getFieldErrors().isEmpty()) {
            return methodArgumentNotValidException.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();
        }
        if (exception instanceof BindException bindException
                && !bindException.getBindingResult().getFieldErrors().isEmpty()) {
            return bindException.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();
        }
        if (exception instanceof MissingServletRequestParameterException missingParameterException) {
            return "Missing request parameter: " + missingParameterException.getParameterName();
        }
        if (exception instanceof MethodArgumentTypeMismatchException mismatchException) {
            return "Request parameter type mismatch: " + mismatchException.getName();
        }
        return exception.getMessage() != null ? exception.getMessage() : "Bad request";
    }
}
