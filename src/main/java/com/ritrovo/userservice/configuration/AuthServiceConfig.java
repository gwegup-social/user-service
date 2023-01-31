package com.ritrovo.userservice.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("authservice.config")
@Data
public class AuthServiceConfig {

    private String baseUrl;
    private String otpInitiationEndpoint;
}
