package com.horace.toolbackend.third;

import com.horace.toolbackend.entity.api.TimeApiEntity;
import com.horace.toolbackend.enums.DateType;
import com.horace.toolbackend.exception.HttpRequestException;
import com.horace.toolbackend.exception.JsonConvertException;
import com.horace.toolbackend.exception.ThirdApiException;
import com.horace.toolbackend.service.JdkHttpClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ThirdTimeApiClientTest {

    private static final String BASE_URL = "https://timor.tech/api/holiday/info";

    private ThirdTimeApiClient thirdTimeApiClient;

    @Mock
    private JdkHttpClientService httpService;

    @BeforeEach
    void setUp() {
        thirdTimeApiClient = new ThirdTimeApiClient(BASE_URL, httpService);
    }

    @Test
    void should_return_entity_when_response_is_valid() {
        when(httpService.get(BASE_URL + "/2026-01-24"))
                .thenReturn("{\"code\":\"0\",\"type\":{\"type\":\"0\"}}");

        TimeApiEntity result = thirdTimeApiClient.getTime("2026-01-24");

        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo("0");
        assertThat(result.getType()).isNotNull();
        assertThat(result.getType().type).isEqualTo(DateType.workDay);
        verify(httpService).get(BASE_URL + "/2026-01-24");
    }

    @Test
    void should_throw_thirdApiException_when_http_request_fails() {
        HttpRequestException cause = new HttpRequestException("HTTP request failed", 500, "boom");
        when(httpService.get(BASE_URL + "/2026-01-24")).thenThrow(cause);

        assertThatThrownBy(() -> thirdTimeApiClient.getTime("2026-01-24"))
                .isInstanceOf(ThirdApiException.class)
                .hasMessage("check time API status code is not good")
                .hasCause(cause);
    }

    @Test
    void should_throw_thirdApiException_when_response_code_is_failure() {
        when(httpService.get(BASE_URL + "/2026-01-24"))
                .thenReturn("{\"code\":\"1\",\"type\":{\"type\":\"0\"}}");

        assertThatThrownBy(() -> thirdTimeApiClient.getTime("2026-01-24"))
                .isInstanceOf(ThirdApiException.class)
                .hasMessage("check time API request failed");
    }

    @Test
    void should_throw_thirdApiException_when_response_type_is_missing() {
        when(httpService.get(BASE_URL + "/2026-01-24"))
                .thenReturn("{\"code\":\"0\",\"type\":null}");

        assertThatThrownBy(() -> thirdTimeApiClient.getTime("2026-01-24"))
                .isInstanceOf(ThirdApiException.class)
                .hasMessage("check time API request failed");
    }

    @Test
    void should_throw_thirdApiException_when_response_date_type_is_missing() {
        when(httpService.get(BASE_URL + "/2026-01-24"))
                .thenReturn("{\"code\":\"0\",\"type\":{}}");

        assertThatThrownBy(() -> thirdTimeApiClient.getTime("2026-01-24"))
                .isInstanceOf(ThirdApiException.class)
                .hasMessage("check time API request failed");
    }

    @Test
    void should_throw_jsonConvertException_when_response_is_not_json() {
        when(httpService.get(BASE_URL + "/2026-01-24")).thenReturn("not-json");

        assertThatThrownBy(() -> thirdTimeApiClient.getTime("2026-01-24"))
                .isInstanceOf(JsonConvertException.class)
                .hasMessage("Failed to parse json");
    }
}
