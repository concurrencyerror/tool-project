package com.horace.toolbackend.service;

import com.horace.toolbackend.entity.RemindEntity;
import com.horace.toolbackend.repository.RemindRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RemindService {

    private final RemindRepository remindRepository;

    private static final Logger log = LoggerFactory.getLogger(RemindService.class);

    public RemindService(RemindRepository remindRepository) {
        this.remindRepository = remindRepository;
    }

    public List<RemindEntity> findRemindEntitiesByTime(LocalDateTime time){
        return remindRepository.findActiveAt(time);
    }
}
