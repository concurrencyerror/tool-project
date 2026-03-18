package com.horace.toolbackend.third;

import com.horace.toolbackend.entity.api.TimeApiEntity;
import com.horace.toolbackend.exception.ThirdApiException;
import com.horace.toolbackend.util.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;

/**
 * Third-party client for checking calendar information.
 *
 * @author Horace
 */
@Service
public class ThirdTimeApiClient {

    private static final Logger log = LoggerFactory.getLogger(ThirdTimeApiClient.class);

    private final RestClient restClient;

    private final String url;

    public ThirdTimeApiClient(RestClient restClient,
                              @Value("${check.time}") String url) {
        this.restClient = restClient;
        this.url = url;
    }

    public TimeApiEntity getTime(String time) {
        final TimeApiEntity entity;
        try {
            entity = restClient.get().uri(url + "/{date}", time).accept(MediaType.APPLICATION_JSON)
                    .retrieve().onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), (req, res) -> {
                        throw new ThirdApiException("check time API request failed with status: " + res.getStatusText());
                    }).body(TimeApiEntity.class);
        } catch (ThirdApiException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new ThirdApiException("check time API request failed", e);
        }

        if (Objects.isNull(entity) || Objects.isNull(entity.getType()) || Objects.isNull(entity.getType().type)
                || "1".equals(entity.getCode())) {
            log.error("check time API request failed,the entity is {}", JacksonUtil.safePrettyValueAsString(entity));
            throw new ThirdApiException("check time API request failed");
        }
        return entity;
    }
}
