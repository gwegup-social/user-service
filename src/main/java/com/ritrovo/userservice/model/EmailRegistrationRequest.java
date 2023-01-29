package com.ritrovo.userservice.model;

import lombok.Data;

@Data
public class EmailRegistrationRequest {
    private String email;
    private String password;
}
