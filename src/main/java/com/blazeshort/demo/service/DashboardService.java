package com.blazeshort.demo.service;

import com.blazeshort.demo.model.dto.DashboardAnalyticsResponse;
import com.blazeshort.demo.model.dto.OverallDashboardAnalyticsResponse;
import com.blazeshort.demo.model.dto.RecentClick;
import com.blazeshort.demo.model.entity.ShortUrl;
import com.blazeshort.demo.model.entity.User;
import com.blazeshort.demo.model.enums.UrlStatus;
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
        ShortUrl url = shortUrlRepository.findByShortCode(shortCode)
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

    public OverallDashboardAnalyticsResponse getOverallDashboardAnalytics(Long userId) {
        // Get all URLs for the user
        User user = User.builder().id(userId).build();
        var allUrls = shortUrlRepository.findAllByUser(user);

        // Count different URL statuses
        long totalUrls = allUrls.size();
        long activeUrls = allUrls.stream()
                .filter(url -> url.getStatus() == UrlStatus.ACTIVE)
                .count();
        long expiredUrls = allUrls.stream()
                .filter(url -> url.getStatus() == UrlStatus.EXPIRED)
                .count();
        long disabledUrls = allUrls.stream()
                .filter(url -> url.getStatus() == UrlStatus.DISABLED)
                .count();

        // Get total clicks
        long totalClicks = allUrls.stream()
                .mapToLong(url -> analyticsRepository.countByShortUrl(url))
                .sum();

        return OverallDashboardAnalyticsResponse.builder()
                .totalUrls(totalUrls)
                .totalClicks(totalClicks)
                .uniqueClicks(analyticsRepository.countUniqueClicksByUser(userId))
                .activeUrls(activeUrls)
                .expiredUrls(expiredUrls)
                .disabledUrls(disabledUrls)
                .clicksPerDay(analyticsRepository.getDailyClicksByUser(userId))
                .topIps(analyticsRepository.topIpsByUser(userId))
                .topUserAgents(analyticsRepository.topUserAgentsByUser(userId))
                .topPerformingUrls(analyticsRepository.getTopPerformingUrlsByUser(userId))
                .recentClicks(
                        analyticsRepository.findTop10RecentClicksByUser(userId)
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
