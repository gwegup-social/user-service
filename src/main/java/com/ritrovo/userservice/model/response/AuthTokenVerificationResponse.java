package com.ritrovo.userservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class AuthTokenVerificationResponse {

    private boolean isValid;
    private String errorMessage;
}
