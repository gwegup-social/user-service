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
        PERSONAL_EMAIL_VERIFICATION_PENDING("PERSONAL_EMAIL_VERIFICATION_PENDING"),
        PERSONAL_EMAIL_VERIFICATION_COMPLETED("PERSONAL_EMAIL_VERIFICATION_COMPLETED"),
        CORPORATE_EMAIL_VERIFICATION_PENDING("CORPORATE_EMAIL_VERIFICATION_PENDING"),
        CORPORATE_EMAIL_VERIFICATION_COMPLETED("CORPORATE_EMAIL_VERIFICATION_COMPLETED"),
        ACTIVE("ACTIVE"),     // corporate verified stage.
        INACTIVE("INACTIVE");   // corporate activity suspecious (i.e user have not verified corporate email in the last 3 month )

        private final String value;

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum Gender {

        FEMALE("FEMALE"),
        MALE("MALE"),
        OTHER("OTHER");
        private final String gender;

        Gender(String value) {
            this.gender = value;
        }

        public String getGender() {
            return gender;
        }
    }

}

