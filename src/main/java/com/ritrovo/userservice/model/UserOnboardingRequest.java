package com.ritrovo.userservice.model;

import lombok.Data;

@Data
public class UserOnboardingRequest {
    private String email;
    private String password;
}
