package com.ritrovo.userservice.controller;


import com.ritrovo.userservice.model.request.EmailRegistrationRequest;
import com.ritrovo.userservice.model.response.LoginResponse;
import com.ritrovo.userservice.service.LoginService;
import com.ritrovo.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class LoginController {

    private final UserService userService;
    private final LoginService loginService;

    public LoginController(UserService userService,
                           LoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
    }


    @PostMapping("/signup")
    @Transactional
    public LoginResponse registerUser(@RequestBody EmailRegistrationRequest emailRegistrationRequest) {
        return userService.onboardUser(emailRegistrationRequest);
    }

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<LoginResponse> login() {
        return ResponseEntity.ok(loginService.getLoginResponse());
    }
}
