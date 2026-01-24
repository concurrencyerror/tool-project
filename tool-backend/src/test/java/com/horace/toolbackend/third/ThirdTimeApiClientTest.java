package com.horace.toolbackend.third;

import com.horace.toolbackend.config.MyRestClientConfig;
import com.horace.toolbackend.exception.ThirdApiException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
        mockServer.expect(MockRestRequestMatchers.requestTo("https://timor.tech/api/holiday/info/2026-01-01"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.INTERNAL_SERVER_ERROR).body("boom"));
        assertThatThrownBy(() -> thirdTimeApiClient.getTime("2026-01-01"))
                .isInstanceOf(ThirdApiException.class);
    }

}