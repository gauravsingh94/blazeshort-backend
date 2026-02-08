package com.blazeshort.demo.controller;


import com.blazeshort.demo.model.enums.UrlStatus;
import com.blazeshort.demo.service.ShortUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/urls")
@RequiredArgsConstructor
public class UrlManagementController {
    private final ShortUrlService shortUrlService;

    @PatchMapping("/{code}/disable")
    public ResponseEntity<Map<String, String>> disableUrl(@PathVariable String code) {
        shortUrlService.updateStatus(code, UrlStatus.DISABLED);
        return ResponseEntity.ok(Map.of("message","URL has been disabled"));
    }

    @PatchMapping("/{code}/enable")
    public ResponseEntity<Map<String, String>> enableUrl(@PathVariable String code) {
        shortUrlService.updateStatus(code, UrlStatus.ACTIVE);
       return  ResponseEntity.ok(Map.of("message","URL has been activated"));
    }

}
