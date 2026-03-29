package com.horace.toolbackend.third;

import com.horace.toolbackend.entity.api.TimeApiEntity;
import com.horace.toolbackend.exception.HttpRequestException;
import com.horace.toolbackend.exception.ThirdApiException;
import com.horace.toolbackend.service.JdkHttpClientService;
import com.horace.toolbackend.util.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Third-party client for checking calendar information.
 *
 * @author Horace
 */
@Service
public class ThirdTimeApiClient {

    private static final Logger log = LoggerFactory.getLogger(ThirdTimeApiClient.class);

    private final String url;

    private final JdkHttpClientService httpService;

    public ThirdTimeApiClient(@Value("${check.time}") String url,
                              JdkHttpClientService httpService) {
        this.url = url;
        this.httpService = httpService;
    }

    public TimeApiEntity getTime(String time) {
        final TimeApiEntity entity;
        try {
            String result = httpService.get(url + "/" + time);
            entity = JacksonUtil.readValue(result, TimeApiEntity.class);
        } catch (HttpRequestException e) {
            throw new ThirdApiException("check time API status code is not good", e);
        }
        if (Objects.isNull(entity) || Objects.isNull(entity.getType()) || Objects.isNull(entity.getType().type)
                || "1".equals(entity.getCode())) {
            log.error("check time API request failed,the entity is {}", JacksonUtil.safePrettyValueAsString(entity));
            throw new ThirdApiException("check time API request failed");
        }
        return entity;
    }
}
