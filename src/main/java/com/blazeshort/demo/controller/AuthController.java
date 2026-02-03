package com.blazeshort.demo.controller;

import com.blazeshort.demo.model.dto.LoginRequest;
import com.blazeshort.demo.model.dto.SignupRequest;
import com.blazeshort.demo.model.entity.User;
import com.blazeshort.demo.repository.UserRepository;
import com.blazeshort.demo.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @PostMapping("/signup")
    public ResponseEntity<?>signup(@RequestBody SignupRequest request){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body(Map.of("message","Username is already in use"));
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message","User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?>login(@RequestBody LoginRequest request){
        User user =  userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new RuntimeException("User not found"));
        if(!encoder.matches(request.getPassword(),user.getPassword())){
            return ResponseEntity.status(401).body(Map.of("message","Invalid credentials"));
        }
        String token = tokenProvider.generateToken(user.getId(),user.getRole());
        return ResponseEntity.ok(Map.of("token",token));
    }
}
