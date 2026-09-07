package com.priyanshu.iims.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.priyanshu.iims.dto.LoginRequest;
import com.priyanshu.iims.dto.LoginResponse;
import com.priyanshu.iims.dto.UserRegistrationRequest;
import com.priyanshu.iims.dto.UserResponse;
import com.priyanshu.iims.model.User;
import com.priyanshu.iims.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody UserRegistrationRequest request) {

        User user = userService.registerUser(
                request.getEmail(),
                request.getPassword()
        );
        UserResponse response=new UserResponse(user.getId(),user.getEmail(),user.getRole().name());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(
            @Valid @RequestBody LoginRequest request) {

        String token = userService.loginUser(
                request.getEmail(),
                request.getPassword()
        );

        return ResponseEntity.ok(new LoginResponse(token));
    }
}