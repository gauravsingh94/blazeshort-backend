package com.blazeshort.demo.controller;

import com.blazeshort.demo.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider tokenProvider;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    public String login(@RequestParam Long userId,
                        @RequestParam String role) {
        return tokenProvider.generateToken(userId, role);
    }
}
