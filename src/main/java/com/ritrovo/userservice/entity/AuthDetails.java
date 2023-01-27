package com.ritrovo.userservice.entity;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("auth_details")
@Data
@Builder
public class AuthDetails {

    @Id
    private String principal;
    private String password;
    private String algorithm;
    private String userId;

}

