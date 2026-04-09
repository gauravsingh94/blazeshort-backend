package com.blazeshort.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TopPerformingUrl {
    private String shortCode;
    private String originalUrl;
    private long totalClicks;
    private long uniqueClicks;
}

