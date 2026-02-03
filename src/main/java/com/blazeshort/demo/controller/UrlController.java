package com.blazeshort.demo.controller;

import com.blazeshort.demo.config.RateLimitConfig;
import com.blazeshort.demo.exception.RateLimitExceededException;
import com.blazeshort.demo.model.dto.ShortenRequest;
import com.blazeshort.demo.model.dto.ShortenResponse;
import com.blazeshort.demo.model.entity.ShortUrl;
import com.blazeshort.demo.service.RateLimitService;
import com.blazeshort.demo.service.ShortUrlService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor

public class UrlController {
    private final ShortUrlService shortUrlService;
    private final RateLimitService rateLimitService;

    @PostMapping("/api/url")
    public ShortenResponse create(@RequestBody ShortenRequest request){
        Long userId = Long.parseLong(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName()
        );

        String key = "rl:create:" + userId;

        if (!rateLimitService.isAllowed(
                key,
                RateLimitConfig.CREATE_URL_LIMIT,
                RateLimitConfig.CREATE_URL_WINDOW_SEC
        )) {
            throw new RateLimitExceededException("Too many URLs created");
        }

        ShortUrl url = shortUrlService.createShortUrl(request,userId);

        return ShortenResponse.builder()
                .originalUrl(url.getOriginalUrl())
                .shortCode(url.getShortCode())
                .status(url.getStatus())
                .createdAt(url.getCreatedAt())
                .expiresAt(url.getExpiresAt())
                .build();
    }

    @GetMapping("/{code}")
    public void redirect(@PathVariable String code, HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        String ip = request.getRemoteAddr();
        String key = "redirect_rate:" + ip;

        if (!rateLimitService.isAllowed(
                key,
                RateLimitConfig.REDIRECT_LIMIT,
                RateLimitConfig.REDIRECT_WINDOW_SEC
        )) {
            throw new RateLimitExceededException("Too many URLs created");
        }


        String  originalUrl= shortUrlService.getOriginalUrl(code);
        response.sendRedirect(originalUrl);
    }

    @GetMapping("/url/my")
    public List<ShortenResponse> myUrls() {

        Long userId = Long.parseLong(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName()
        );

        return shortUrlService.getUserUrls(userId)
                .stream()
                .map(url -> ShortenResponse.builder()
                        .originalUrl(url.getOriginalUrl())
                        .shortCode(url.getShortCode())
                        .status(url.getStatus())
                        .createdAt(url.getCreatedAt())
                        .expiresAt(url.getExpiresAt())
                        .build())
                .toList();
    }

}