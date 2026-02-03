package com.blazeshort.demo.config;

public class RateLimitConfig {

    public static final int REDIRECT_LIMIT = 3;
    public static final int REDIRECT_WINDOW_SEC = 60;

    public static final int CREATE_URL_LIMIT = 10;
    public static final int CREATE_URL_WINDOW_SEC= 60;

    private RateLimitConfig(){}
}
