package com.horace.toolbackend.service;

import com.horace.toolbackend.api.CheckTimeService;
import com.horace.toolbackend.entity.RemindEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 从数据库中获取数据检查时间的实现
 */
@Service
public class DBCheckTimeService implements CheckTimeService {

    private static final Logger log = LoggerFactory.getLogger(DBCheckTimeService.class);

    private final RemindService remindService;

    public DBCheckTimeService(RemindService remindService) {
        this.remindService = remindService;
    }


    /**
     *
     * @param time
     * @return 如果需要上班或者提醒就是true，不需要就是false
     */
    @Override
    public boolean checkTime(LocalDateTime time) {
        List<RemindEntity> timeList = remindService.findRemindEntitiesByTime(time);
        return !timeList.isEmpty();
    }
}
