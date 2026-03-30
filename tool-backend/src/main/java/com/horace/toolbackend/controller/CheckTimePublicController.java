package com.horace.toolbackend.controller;

import com.horace.toolbackend.service.CheckTimeFacadeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/check-time")
public class CheckTimePublicController {


    private final CheckTimeFacadeService checkTimeFacadeService;

    public CheckTimePublicController(CheckTimeFacadeService checkTimeFacadeService) {
        this.checkTimeFacadeService = checkTimeFacadeService;
    }

    @GetMapping("/{checkTime}")
    public boolean checkTime(@PathVariable String checkTime) {
        return checkTimeFacadeService.checkTime(checkTime);
    }
}
