package com.ritrovo.userservice.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OtpInitiationRequest {
    private String destination;
}
