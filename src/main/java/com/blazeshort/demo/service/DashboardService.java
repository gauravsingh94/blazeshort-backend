package com.blazeshort.demo.service;

import com.blazeshort.demo.model.dto.DashboardAnalyticsResponse;
import com.blazeshort.demo.model.dto.RecentClick;
import com.blazeshort.demo.model.entity.ShortUrl;
import com.blazeshort.demo.repository.ShortUrlRepository;
import com.blazeshort.demo.repository.UrlAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final ShortUrlRepository shortUrlRepository;
    private final UrlAnalyticsRepository analyticsRepository;

    public DashboardAnalyticsResponse getDashboardAnalytics(
            String shortCode,
            Long userId
    ) {
        ShortUrl url = shortUrlRepository.findByShortCodeAndUserId(shortCode,userId)
                .orElseThrow(() -> new RuntimeException("URL not found"));

        if (!url.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        return DashboardAnalyticsResponse.builder()
                .shortCode(shortCode)
                .totalClicks(analyticsRepository.countByShortUrl(url))
                .uniqueClicks(analyticsRepository.countUniqueClicks(url))
                .clicksPerDay(analyticsRepository.getDailyClicks(url))
                .topIps(analyticsRepository.topIps(url))
                .topUserAgents(analyticsRepository.topUserAgents(url))
                .recentClicks(
                        analyticsRepository.findTop10ByShortUrlOrderByCreatedAtDesc(url)
                                .stream()
                                .map(a -> new RecentClick(
                                        a.getIpAddress(),
                                        a.getUserAgent(),
                                        a.getCreatedAt()
                                ))
                                .toList()
                )
                .build();
    }
}
