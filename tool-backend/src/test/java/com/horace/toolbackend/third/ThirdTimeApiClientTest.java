package com.horace.toolbackend.third;

import com.horace.toolbackend.config.MyRestClientConfig;
import com.horace.toolbackend.entity.api.TimeApiEntity;
import com.horace.toolbackend.exception.ThirdApiException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@RestClientTest(ThirdTimeApiClient.class)
@Import(MyRestClientConfig.class)
class ThirdTimeApiClientTest {


    @Autowired
    ThirdTimeApiClient thirdTimeApiClient;

    @Autowired
    MockRestServiceServer mockServer;

    @Test
    void checkTime_shouldThrowThirdApiException_when500() {
        mockServer.expect(MockRestRequestMatchers.requestToUriTemplate("https://timor.tech/api/holiday/info/{date}"
                        , "2026-01-24"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andExpect(MockRestRequestMatchers.header("Accept", "application/json"))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.INTERNAL_SERVER_ERROR).body("boom"));
        assertThatThrownBy(() -> thirdTimeApiClient.getTime("2026-01-24"))
                .isInstanceOf(ThirdApiException.class);
    }

    @Test
    void checkTime_shouldReturnTimeEntity_when200() {
        mockServer.expect(MockRestRequestMatchers.requestToUriTemplate("https://timor.tech/api/holiday/info/{date}"
                        , "2026-01-24"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andExpect(MockRestRequestMatchers.header("Accept", "application/json"))
                .andRespond(MockRestResponseCreators.withSuccess("{\"code\":\"0\",\"type\":{\"type\":\"0\"}}", MediaType.APPLICATION_JSON));
        Assertions.assertThat(thirdTimeApiClient.getTime("2026-01-24"))
                .isInstanceOf(TimeApiEntity.class);
    }

    //每次之后进行检查
    @AfterEach
    void afterCheck() {
        mockServer.verify();
    }

}