package com.ritrovo.userservice.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private String userId;
    private String status;
    private String displayName;
    private String orgName;
    private String accessToken;
    private String refreshToken;
}
