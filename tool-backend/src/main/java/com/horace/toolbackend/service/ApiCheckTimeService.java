package com.horace.toolbackend.service;

import com.horace.toolbackend.api.CheckTimeService;
import com.horace.toolbackend.entity.api.TimeApiEntity;
import com.horace.toolbackend.enums.DateType;
import com.horace.toolbackend.third.ThirdTimeApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 根据第三方 api 判断时间的service
 */
@Service
public class ApiCheckTimeService implements CheckTimeService {

    private final ThirdTimeApiClient restClient;

    private static final Logger log = LoggerFactory.getLogger(ApiCheckTimeService.class);

    public ApiCheckTimeService(ThirdTimeApiClient restClient) {
        this.restClient = restClient;
    }

    /**
     * 根据第三方 api 判断时间是否上班
     *
     * @param time 传入的时间
     * @return 是否上班
     */
    @Override
    public boolean checkTime(LocalDateTime time) {
        String formattedTime = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        try {
            TimeApiEntity entity = restClient.getTime(formattedTime);
            return entity.getType().type == DateType.workDay || entity.getType().type == DateType.change;
        } catch (Exception e) {
            log.error("check time API request failed,the exception is ", e);
            //如果失败默认上班，这样可以促使我去检查代码和日志
            return true;
        }
    }
}
