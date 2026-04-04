package com.blazeshort.demo.controller;


import com.blazeshort.demo.model.enums.UrlStatus;
import com.blazeshort.demo.service.ShortUrlService;
import com.blazeshort.demo.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/urls")
@RequiredArgsConstructor
public class UrlManagementController {
    private final ShortUrlService shortUrlService;

    @PatchMapping("/{code}/disable")
    public ResponseEntity<Map<String, String>> disableUrl(@PathVariable String code) {
        Long userId = SecurityUtils.getCurrentUserId();
        shortUrlService.updateStatus(code, UrlStatus.DISABLED,userId);
        return ResponseEntity.ok(Map.of("message","URL has been disabled"));
    }

    @PatchMapping("/{code}/enable")
    public ResponseEntity<Map<String, String>> enableUrl(@PathVariable String code) {
        Long userId = SecurityUtils.getCurrentUserId();
        shortUrlService.updateStatus(code, UrlStatus.ACTIVE,userId);
       return  ResponseEntity.ok(Map.of("message","URL has been activated"));
    }
    @DeleteMapping("/{code}")
    public ResponseEntity<Map<String, String>> deleteUrl(@PathVariable String code) {
        Long userId = SecurityUtils.getCurrentUserId();
        shortUrlService.deleteByShortCode(code,userId);
        return ResponseEntity.ok(Map.of("message","URL has been deleted"));
    }

}
