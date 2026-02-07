package com.blazeshort.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IpStats {
    private String ip;
    private long count;
}
