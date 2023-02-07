package com.ritrovo.userservice.model.request;

import com.ritrovo.userservice.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

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
