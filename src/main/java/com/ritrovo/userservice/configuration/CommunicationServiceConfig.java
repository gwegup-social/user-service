package com.ritrovo.userservice.configuration;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class CommunicationServiceConfig {

    private String baseUrl;
    private String otpEmailEndPoint;

}
