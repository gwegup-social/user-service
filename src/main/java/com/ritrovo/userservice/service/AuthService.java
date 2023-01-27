package com.ritrovo.userservice.service;

import com.ritrovo.userservice.dao.AuthDetailsRepository;
import com.ritrovo.userservice.entity.AuthDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class AuthService {

    private final AuthDetailsRepository authDetailsRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthDetailsRepository authDetailsRepository,
                       PasswordEncoder passwordEncoder) {
        this.authDetailsRepository = authDetailsRepository;
        this.passwordEncoder = passwordEncoder;
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
}
