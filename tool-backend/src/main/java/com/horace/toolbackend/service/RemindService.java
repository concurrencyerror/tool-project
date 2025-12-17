package com.horace.toolbackend.service;

import com.horace.toolbackend.repository.RemindRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RemindService {

    private final RemindRepository remindRepository;

    private static final Logger log = LoggerFactory.getLogger(RemindService.class);

    public RemindService(RemindRepository remindRepository) {
        this.remindRepository = remindRepository;
    }


}
