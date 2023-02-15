package com.ritrovo.userservice.service;

import com.google.common.collect.Maps;
import com.ritrovo.userservice.configuration.AuthServiceConfig;
import com.ritrovo.userservice.dao.AuthDetailsRepository;
import com.ritrovo.userservice.entity.AuthDetails;
import com.ritrovo.userservice.model.request.OtpInitiationRequest;
import com.ritrovo.userservice.model.response.OtpStatusResponse;
import com.ritrovo.userservice.util.RestClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;

@Service
public class AuthService {

    private final AuthDetailsRepository authDetailsRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestClient restClient;
    private final AuthServiceConfig authServiceConfig;

    public AuthService(AuthDetailsRepository authDetailsRepository,
                       PasswordEncoder passwordEncoder,
                       RestClient restClient,
                       AuthServiceConfig authServiceConfig) {
        this.authDetailsRepository = authDetailsRepository;
        this.passwordEncoder = passwordEncoder;
        this.restClient = restClient;
        this.authServiceConfig = authServiceConfig;
    }

    public boolean initialiseUserCredentials(String userId,
                                             String principal,
                                             String passwordBase64) {

        byte[] decodedBytes = Base64.getDecoder().decode(passwordBase64);
        String password = new String(decodedBytes);
        String hashedPassword = passwordEncoder.encode(password);

        AuthDetails authDetails = AuthDetails
                .builder()
                .userId(userId)
                .principal(principal)
                .password(hashedPassword)
                .algorithm("Bcrypt")
                .build();

        authDetailsRepository.insert(authDetails);
        return true;
    }

    public void sendOtpOverEmail(String email) {

        String baseUrl = authServiceConfig.getBaseUrl();
        String otpInitiationEndpoint = authServiceConfig.getOtpInitiationEndpoint();

        OtpInitiationRequest otpInitiationRequest = OtpInitiationRequest
                .builder()
                .destination(email)
                .build();

        String url = baseUrl.concat(otpInitiationEndpoint);
        restClient.postForEntity(url, otpInitiationRequest, String.class);
    }

    public OtpStatusResponse getOtpRequestStatus(String otpRequestId) {
        String baseUrl = authServiceConfig.getBaseUrl();
        String otpInitiationEndpoint = authServiceConfig.getOtpStatusEndpoint();
        String url = baseUrl.concat(otpInitiationEndpoint);

        Map<String, Object> uriVariables = Maps.newHashMap();
        uriVariables.put("requestId", otpRequestId);

        return restClient.getForEntity(url, OtpStatusResponse.class, uriVariables);
    }
}
