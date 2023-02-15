package com.ritrovo.userservice.controller;


import com.ritrovo.userservice.model.request.EmailRegistrationRequest;
import com.ritrovo.userservice.model.response.EmailRegistrationResponse;
import com.ritrovo.userservice.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    @Transactional
    public EmailRegistrationResponse registerUser(@RequestBody EmailRegistrationRequest emailRegistrationRequest) {
        return userService.onboardUser(emailRegistrationRequest);
    }

}
