package com.blazeshort.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAgentStats {
    private String userAgent;
    private long count;
}
