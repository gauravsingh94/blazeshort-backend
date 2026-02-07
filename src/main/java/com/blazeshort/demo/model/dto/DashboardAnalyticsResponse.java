package com.blazeshort.demo.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DashboardAnalyticsResponse {
    private String shortCode;
    private long totalClicks;
    private long uniqueClicks;

    // charts
    private List<DailyClickCount> clicksPerDay;

    // tables
    private List<IpStats> topIps;
    private List<UserAgentStats> topUserAgents;

    private List<RecentClick> recentClicks;
}
