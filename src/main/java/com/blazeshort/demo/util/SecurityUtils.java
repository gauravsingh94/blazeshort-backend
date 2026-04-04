package com.blazeshort.demo.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getName())) {
            throw new RuntimeException("User not authenticated");
        }

        try {
            return Long.parseLong(auth.getName());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid user ID in authentication");
        }
    }
}
