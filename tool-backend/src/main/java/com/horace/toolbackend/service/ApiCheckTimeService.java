package com.horace.toolbackend.service;

import com.horace.toolbackend.api.CheckTimeService;
import com.horace.toolbackend.exception.ThirdApiException;
import com.horace.toolbackend.util.JacksonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 根据第三方 api 判断时间的service
 */
@Service
public class ApiCheckTimeService implements CheckTimeService {

    private final RestClient restClient;
    @Value("${check.time}")
    private String url;

    public ApiCheckTimeService(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * 根据第三方 api 判断时间是否有效
     *
     * @param time 传入的时间
     * @return 是否有效
     */
    @Override
    public boolean checkTime(LocalDateTime time) {
        String formattedTime = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        ResponseEntity<String> entity = restClient.get().uri(url + "/{date}", formattedTime).accept(MediaType.APPLICATION_JSON)
                .retrieve().onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), (req, res) -> {
                    throw new ThirdApiException("check time API request failed with status: " + res.getStatusText());
                }).toEntity(String.class);
    }
}
