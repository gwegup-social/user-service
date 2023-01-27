package com.ritrovo.userservice.controller;


import com.ritrovo.userservice.model.UserOnboardingRequest;
import com.ritrovo.userservice.service.UserService;
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
    public void registerUser(@RequestBody UserOnboardingRequest userOnboardingRequest) {
        userService.onboardUser(userOnboardingRequest);
    }

}
