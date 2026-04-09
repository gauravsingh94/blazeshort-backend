package com.blazeshort.demo.repository;

import com.blazeshort.demo.model.dto.DailyClickCount;
import com.blazeshort.demo.model.dto.IpStats;
import com.blazeshort.demo.model.dto.UserAgentStats;
import com.blazeshort.demo.model.dto.TopPerformingUrl;
import com.blazeshort.demo.model.entity.ShortUrl;
import com.blazeshort.demo.model.entity.UrlAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UrlAnalyticsRepository extends JpaRepository<UrlAnalytics, Long> {

    long countByShortUrl(ShortUrl shortUrl);

    @Query("SELECT COUNT(DISTINCT a.ipAddress) FROM UrlAnalytics a WHERE a.shortUrl = :url")
    long countUniqueClicks(@Param("url") ShortUrl url);

    @Query("""
    SELECT new com.blazeshort.demo.model.dto.DailyClickCount(
        CAST(a.createdAt AS LocalDate),
        COUNT(a)
    )
    FROM UrlAnalytics a
    WHERE a.shortUrl = :url
    GROUP BY CAST(a.createdAt AS LocalDate)
    ORDER BY CAST(a.createdAt AS LocalDate)
""")
    List<DailyClickCount> getDailyClicks(@Param("url") ShortUrl url);

    @Query("""
        SELECT new com.blazeshort.demo.model.dto.IpStats(
            a.ipAddress, COUNT(a)
        )
        FROM UrlAnalytics a
        WHERE a.shortUrl = :url
        GROUP BY a.ipAddress
        ORDER BY COUNT(a) DESC
    """)
    List<IpStats> topIps(@Param("url") ShortUrl url);

    @Query("""
        SELECT new com.blazeshort.demo.model.dto.UserAgentStats(
            a.userAgent, COUNT(a)
        )
        FROM UrlAnalytics a
        WHERE a.shortUrl = :url
        GROUP BY a.userAgent
        ORDER BY COUNT(a) DESC
    """)
    List<UserAgentStats> topUserAgents(@Param("url") ShortUrl url);

    List<UrlAnalytics> findTop10ByShortUrlOrderByCreatedAtDesc(ShortUrl shortUrl);

    // Overall analytics queries for dashboard
    @Query("""
    SELECT COUNT(DISTINCT a.ipAddress) FROM UrlAnalytics a 
    JOIN a.shortUrl su 
    WHERE su.user.id = :userId
    """)
    long countUniqueClicksByUser(@Param("userId") Long userId);

    @Query("""
    SELECT new com.blazeshort.demo.model.dto.DailyClickCount(
        CAST(a.createdAt AS LocalDate),
        COUNT(a)
    )
    FROM UrlAnalytics a
    JOIN a.shortUrl su
    WHERE su.user.id = :userId
    GROUP BY CAST(a.createdAt AS LocalDate)
    ORDER BY CAST(a.createdAt AS LocalDate)
    """)
    List<DailyClickCount> getDailyClicksByUser(@Param("userId") Long userId);

    @Query("""
        SELECT new com.blazeshort.demo.model.dto.IpStats(
            a.ipAddress, COUNT(a)
        )
        FROM UrlAnalytics a
        JOIN a.shortUrl su
        WHERE su.user.id = :userId
        GROUP BY a.ipAddress
        ORDER BY COUNT(a) DESC
        LIMIT 10
    """)
    List<IpStats> topIpsByUser(@Param("userId") Long userId);

    @Query("""
        SELECT new com.blazeshort.demo.model.dto.UserAgentStats(
            a.userAgent, COUNT(a)
        )
        FROM UrlAnalytics a
        JOIN a.shortUrl su
        WHERE su.user.id = :userId
        GROUP BY a.userAgent
        ORDER BY COUNT(a) DESC
        LIMIT 10
    """)
    List<UserAgentStats> topUserAgentsByUser(@Param("userId") Long userId);

    @Query("""
        SELECT new com.blazeshort.demo.model.dto.TopPerformingUrl(
            su.shortCode, su.originalUrl, COUNT(a), COUNT(DISTINCT a.ipAddress)
        )
        FROM UrlAnalytics a
        JOIN a.shortUrl su
        WHERE su.user.id = :userId
        GROUP BY su.id, su.shortCode, su.originalUrl
        ORDER BY COUNT(a) DESC
        LIMIT 10
    """)
    List<TopPerformingUrl> getTopPerformingUrlsByUser(@Param("userId") Long userId);

    @Query("""
        SELECT a FROM UrlAnalytics a
        JOIN a.shortUrl su
        WHERE su.user.id = :userId
        ORDER BY a.createdAt DESC
        LIMIT 10
    """)
    List<UrlAnalytics> findTop10RecentClicksByUser(@Param("userId") Long userId);
}


