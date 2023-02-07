package com.ritrovo.userservice.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserDto {

    private String userId;
    private String displayName;
    private String phone;
    private String personalEmail;
    private String corporateEmail;
    private String status;
    private Boolean loginDisabled;
    private String companyName;
    private String gender;
    private String dateOfBirth;
}
