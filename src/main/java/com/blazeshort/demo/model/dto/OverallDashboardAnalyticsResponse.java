package com.blazeshort.demo.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OverallDashboardAnalyticsResponse {
    private long totalUrls;
    private long totalClicks;
    private long uniqueClicks;
    private long activeUrls;
    private long expiredUrls;
    private long disabledUrls;
    
    // charts
    private List<DailyClickCount> clicksPerDay;
    
    // tables
    private List<IpStats> topIps;
    private List<UserAgentStats> topUserAgents;
    
    // Top performing URLs
    private List<TopPerformingUrl> topPerformingUrls;
    
    private List<RecentClick> recentClicks;
}

