package com.horace.toolbackend.controller;

import com.horace.toolbackend.service.CheckTimeFacadeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日期校验公开接口。
 */
@RestController
@RequestMapping("/api/public/check-time")
public class CheckTimePublicController {


    private final CheckTimeFacadeService checkTimeFacadeService;

    public CheckTimePublicController(CheckTimeFacadeService checkTimeFacadeService) {
        this.checkTimeFacadeService = checkTimeFacadeService;
    }

    /**
     * 校验指定日期是否满足业务可用日期规则。
     *
     * <p>该接口位于 /api/public 下，不需要登录即可访问。</p>
     *
     * @param checkTime 待校验的日期字符串
     * @return true 表示日期可用，false 表示日期不可用
     */
    @GetMapping("/{checkTime}")
    public boolean checkTime(@PathVariable String checkTime) {
        return checkTimeFacadeService.checkTime(checkTime);
    }
}
