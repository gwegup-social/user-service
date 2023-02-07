package com.ritrovo.userservice.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailRegistrationResponse {

    private String status;
    private String userId;
}
