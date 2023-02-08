package com.ritrovo.userservice.model.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdateUserProfileRequest {

    private String userId;
    private String name;
    private String dateOfBirth;
    private String gender;
    private String corporateEmail;
    private String phoneNumber;
}
