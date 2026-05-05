package com.horace.toolbackend.service.config;

import com.horace.toolbackend.entity.RemindEntity;
import com.horace.toolbackend.service.RemindService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 关于提醒的配置接口
 */
@Service
public class RemindFacadeService {
    private final RemindService remindService;

    public RemindFacadeService(RemindService remindService) {
        this.remindService = remindService;
    }


    public Page<RemindEntity> findAll(Pageable pageable) {
        return remindService.findAll(pageable);
    }

    public RemindEntity save(RemindEntity remindEntity) {
        return remindService.save(remindEntity);
    }

    public void deleteById(Long id) {
        remindService.deleteById(id);
    }

    public RemindEntity findById(Long id) {
        return remindService.findById(id);
    }

    public RemindEntity update(RemindEntity remindEntity) {
        return remindService.update(remindEntity);
    }

    public RemindEntity saveAndFlush(RemindEntity remindEntity) {
        return remindService.save(remindEntity);
    }
}
