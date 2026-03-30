package com.horace.toolbackend.service;

import com.horace.toolbackend.api.CheckTimeService;
import com.horace.toolbackend.entity.api.TimeApiEntity;
import com.horace.toolbackend.enums.DateType;
import com.horace.toolbackend.exception.ThirdApiException;
import com.horace.toolbackend.third.ThirdTimeApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Check workday information through the third-party API.
 */
@Service
public class ApiCheckTimeService implements CheckTimeService {

    private static final Logger log = LoggerFactory.getLogger(ApiCheckTimeService.class);

    private final ThirdTimeApiClient restClient;

    public ApiCheckTimeService(ThirdTimeApiClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public boolean checkTime(LocalDate time) {
        String formattedTime = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        try {
            TimeApiEntity entity = restClient.getTime(formattedTime);
            if (entity == null || entity.getType() == null || entity.getType().type == null) {
                throw new ThirdApiException("check time API returned invalid entity");
            }
            return entity.getType().type == DateType.workDay || entity.getType().type == DateType.change;
        } catch (ThirdApiException e) {
            log.error("check time API request failed,the exception is ", e);
            return true;
        }
    }
}
