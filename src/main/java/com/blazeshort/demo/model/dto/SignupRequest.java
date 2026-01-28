package com.blazeshort.demo.model.dto;

import com.blazeshort.demo.model.enums.Role;
import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String password;
    private Role role;
}


