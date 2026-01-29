package com.blazeshort.demo.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ShortenRequest {
    private String originalUrl;
    private LocalDateTime expiresAt;
}


