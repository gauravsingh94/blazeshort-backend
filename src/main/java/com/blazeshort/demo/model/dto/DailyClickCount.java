package com.blazeshort.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DailyClickCount {
    private LocalDate date;
    private long count;
}
