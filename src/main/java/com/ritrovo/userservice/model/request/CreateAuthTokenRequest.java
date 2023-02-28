package com.ritrovo.userservice.model.request;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class CreateAuthTokenRequest {

    private String userId;
    private Map<String, String> claims;
}
