package com.ritrovo.userservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpStatusResponse {

    private String requestId;
    private String communicationType;
    private String destination;
    private String otpVerificationStatus;
}
