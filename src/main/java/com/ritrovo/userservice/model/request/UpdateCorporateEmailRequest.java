package com.ritrovo.userservice.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCorporateEmailRequest {

    private String userId;
    private String corporateEmail;
    private String otpRequestId;
}
