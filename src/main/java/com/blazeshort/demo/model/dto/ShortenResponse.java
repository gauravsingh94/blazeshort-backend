package com.blazeshort.demo.model.dto;

import com.blazeshort.demo.model.enums.UrlStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ShortenResponse {
    private String originalUrl;
    private String shortCode;
    private UrlStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
