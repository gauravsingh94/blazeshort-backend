package com.blazeshort.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RecentClick {
    private String ip;
    private String userAgent;
    private LocalDateTime clickedAt;
}
