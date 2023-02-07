package com.ritrovo.userservice.model.request;

import lombok.Data;

@Data
public class EmailRegistrationRequest {
    private String email;
    private String password;
}
