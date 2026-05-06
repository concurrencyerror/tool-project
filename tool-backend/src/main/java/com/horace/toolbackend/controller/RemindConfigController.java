package com.horace.toolbackend.controller;

import com.horace.toolbackend.controller.response.ApiSuccessResponse;
import com.horace.toolbackend.entity.RemindEntity;
import com.horace.toolbackend.service.config.RemindFacadeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 时间提醒配置管理接口。
 *
 * <p>该控制器下的接口都需要登录后访问，用于维护 {@link RemindEntity} 配置。</p>
 */
@RestController
@RequestMapping("/api/remind-config")
public class RemindConfigController {

    private final RemindFacadeService remindFacadeService;

    public RemindConfigController(RemindFacadeService remindFacadeService) {
        this.remindFacadeService = remindFacadeService;
    }

    /**
     * 分页查询时间提醒配置。
     *
     * <p>前端传入的 page 从 1 开始，内部转换为 Spring Data 使用的 0-based 页码。</p>
     *
     * @param page 当前页码，从 1 开始
     * @param size 每页记录数
     * @return 按 createTime 倒序排列的分页数据
     */
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

    /**
     * 新增时间提醒配置。
     *
     * <p>创建时间由后端使用服务器当前时间生成，前端不需要传入。</p>
     *
     * @param remindEntity 待新增的提醒配置
     * @return 保存后的提醒配置
     */
    @PostMapping
    public ApiSuccessResponse<RemindEntity> save(@RequestBody RemindEntity remindEntity) {
        remindEntity.setId(null);
        remindEntity.setCreateTime(LocalDateTime.now());
        return ApiSuccessResponse.success("Create success", remindFacadeService.save(remindEntity));
    }

    /**
     * 根据 id 查询时间提醒配置。
     *
     * @param id 提醒配置 id
     * @return 指定 id 对应的提醒配置
     */
    @GetMapping("/{id}")
    public ApiSuccessResponse<RemindEntity> findById(@PathVariable Long id) {
        return ApiSuccessResponse.success("Query success", findExisting(id));
    }

    /**
     * 根据 id 修改时间提醒配置。
     *
     * <p>路径中的 id 是最终生效的配置 id，请求体中的 id 会被路径 id 覆盖。
     * 创建时间会沿用原记录，不允许前端修改。</p>
     *
     * @param id           提醒配置 id
     * @param remindEntity 修改后的提醒配置内容
     * @return 修改后的提醒配置
     */
    @PutMapping("/{id}")
    public ApiSuccessResponse<RemindEntity> update(@PathVariable Long id,
                                                   @RequestBody RemindEntity remindEntity) {
        RemindEntity existing = findExisting(id);
        remindEntity.setId(id);
        remindEntity.setCreateTime(existing.getCreateTime());
        return ApiSuccessResponse.success("Update success", remindFacadeService.update(remindEntity));
    }

    /**
     * 根据 id 删除时间提醒配置。
     *
     * @param id 提醒配置 id
     * @return 空数据成功响应
     */
    @DeleteMapping("/{id}")
    public ApiSuccessResponse<Void> deleteById(@PathVariable Long id) {
        findExisting(id);
        remindFacadeService.deleteById(id);
        return ApiSuccessResponse.success("Delete success", null);
    }

    /**
     * 查询指定 id 的配置；不存在时抛出参数异常，由全局异常处理器转换为错误响应。
     */
    private RemindEntity findExisting(Long id) {
        RemindEntity remindEntity = remindFacadeService.findById(id);
        if (remindEntity == null) {
            throw new IllegalArgumentException("Remind config does not exist: " + id);
        }
        return remindEntity;
    }
}
