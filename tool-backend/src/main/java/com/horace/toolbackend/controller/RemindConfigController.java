package com.horace.toolbackend.controller;

import com.horace.toolbackend.controller.response.ApiSuccessResponse;
import com.horace.toolbackend.entity.RemindEntity;
import com.horace.toolbackend.service.config.RemindFacadeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/remind-config")
public class RemindConfigController {

    private final RemindFacadeService remindFacadeService;

    public RemindConfigController(RemindFacadeService remindFacadeService) {
        this.remindFacadeService = remindFacadeService;
    }

    @GetMapping
    public ApiSuccessResponse<Page<RemindEntity>> page(@RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        if (page <= 0) {
            throw new IllegalArgumentException("page must be greater than 0");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("size must be greater than 0");
        }

        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return ApiSuccessResponse.success("Query success", remindFacadeService.findAll(pageRequest));
    }

    @PostMapping
    public ApiSuccessResponse<RemindEntity> save(@RequestBody RemindEntity remindEntity) {
        return ApiSuccessResponse.success("Create success", remindFacadeService.save(remindEntity));
    }

    @GetMapping("/{id}")
    public ApiSuccessResponse<RemindEntity> findById(@PathVariable Long id) {
        return ApiSuccessResponse.success("Query success", findExisting(id));
    }

    @PutMapping("/{id}")
    public ApiSuccessResponse<RemindEntity> update(@PathVariable Long id,
                                                   @RequestBody RemindEntity remindEntity) {
        findExisting(id);
        remindEntity.setId(id);
        return ApiSuccessResponse.success("Update success", remindFacadeService.update(remindEntity));
    }

    @DeleteMapping("/{id}")
    public ApiSuccessResponse<Void> deleteById(@PathVariable Long id) {
        findExisting(id);
        remindFacadeService.deleteById(id);
        return ApiSuccessResponse.success("Delete success", null);
    }

    private RemindEntity findExisting(Long id) {
        RemindEntity remindEntity = remindFacadeService.findById(id);
        if (remindEntity == null) {
            throw new IllegalArgumentException("Remind config does not exist: " + id);
        }
        return remindEntity;
    }
}
