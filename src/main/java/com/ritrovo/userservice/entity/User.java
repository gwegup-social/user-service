package com.ritrovo.userservice.entity;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document("user")
@Data
@Builder
public class User {

    @Id
    private String userId;

    private String firstName;

    private String lastName;

    private String phone;

    private String personalEmail;

    private String corporateEmail;

    private Status status;

    private Boolean loginDisabled;

    private String companyName;

    private Gender gender;

    private LocalDate dateOfBirth;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime lastUpdated = LocalDateTime.now();


    public enum Status {
        ONBOARDED("ONBOARDED"),
        PROFILE_UPDATE_PENDING("PROFILE_UPDATE_PENDING"),
        CORPORATE_EMAIL_VERIFICATION_PENDING("CORPORATE_EMAIL_VERIFICATION_PENDING"),
        VERIFIED("VERIFIED");

        private final String value;

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum Gender {

        FEMALE("Female"),
        MALE("Male"),
        OTHER("Others");
        private final String gender;

        Gender(String value) {
            this.gender = value;
        }

        public String getGender() {
            return gender;
        }
    }

}

