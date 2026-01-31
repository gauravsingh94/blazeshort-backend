package com.blazeshort.demo.controller;

import com.blazeshort.demo.config.SecurityConfig;
import com.blazeshort.demo.model.dto.ShortenRequest;
import com.blazeshort.demo.model.dto.ShortenResponse;
import com.blazeshort.demo.model.entity.ShortUrl;
import com.blazeshort.demo.service.ShortUrlService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.DoubleStream;

@RestController
@RequiredArgsConstructor

public class UrlController {
    private final ShortUrlService shortUrlService;

    @PostMapping("/api/url")
    public ShortenResponse create(@RequestBody ShortenRequest request){
        Long userId = Long.parseLong(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName()
        );
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
    public void redirect(@PathVariable String code,
                         HttpServletResponse response) throws IOException {

        ShortUrl url = shortUrlService.getAndValidate(code);
        response.sendRedirect(url.getOriginalUrl());
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