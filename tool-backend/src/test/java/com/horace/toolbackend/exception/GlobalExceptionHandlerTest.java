package com.horace.toolbackend.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldHandleThirdApiException() throws Exception {
        mockMvc.perform(get("/test/third-api"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.status").value(502))
                .andExpect(jsonPath("$.error").value("Bad Gateway"))
                .andExpect(jsonPath("$.message").value("upstream service is unavailable"))
                .andExpect(jsonPath("$.path").value("/test/third-api"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldHandleIllegalArgumentException() throws Exception {
        mockMvc.perform(get("/test/illegal-argument"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("date type is invalid"))
                .andExpect(jsonPath("$.path").value("/test/illegal-argument"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldHandleTypeMismatchException() throws Exception {
        mockMvc.perform(get("/test/type-mismatch").param("value", "not-a-number"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Request parameter type mismatch: value"))
                .andExpect(jsonPath("$.path").value("/test/type-mismatch"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldHandleUnexpectedException() throws Exception {
        mockMvc.perform(get("/test/unexpected"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Internal server error"))
                .andExpect(jsonPath("$.path").value("/test/unexpected"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @RestController
    static class TestController {

        @GetMapping("/test/third-api")
        String throwThirdApiException() {
            throw new ThirdApiException("upstream service is unavailable");
        }

        @GetMapping("/test/illegal-argument")
        String throwIllegalArgumentException() {
            throw new IllegalArgumentException("date type is invalid");
        }

        @GetMapping("/test/type-mismatch")
        String typeMismatch(@RequestParam Integer value) {
            return String.valueOf(value);
        }

        @GetMapping("/test/unexpected")
        String throwUnexpectedException() {
            throw new RuntimeException("boom");
        }
    }
}
