package com.horace.toolbackend.service;

import com.horace.toolbackend.api.CheckTimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class CheckTimeFacadeService {

    private static final Logger log = LoggerFactory.getLogger(CheckTimeFacadeService.class);

    private final CheckTimeService dbCheckTimeService;

    private final CheckTimeService apiCheckTimeService;

    public CheckTimeFacadeService(@Qualifier("DBCheckTimeService") CheckTimeService dbCheckTimeService,
                                  @Qualifier("apiCheckTimeService") CheckTimeService apiCheckTimeService) {
        this.dbCheckTimeService = dbCheckTimeService;
        this.apiCheckTimeService = apiCheckTimeService;
    }


    public boolean checkTime(String checkTime) {
        LocalDate time = LocalDate.parse(checkTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        log.info("check time api level: {}", time);
        boolean dbResult = dbCheckTimeService.checkTime(time);
        boolean apiResult = apiCheckTimeService.checkTime(time);
        return dbResult || apiResult;
    }
}
