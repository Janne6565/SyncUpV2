package com.janne.syncupv2.controller.demo;

import com.janne.syncupv2.controller.ApiController;
import com.janne.syncupv2.model.dto.RegisterUserRequestDto;
import com.janne.syncupv2.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController extends ApiController {

    private final AuthService authService;

    @PostMapping("/user")
    public ResponseEntity<String> createUser(@Valid @RequestBody RegisterUserRequestDto registerUserRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(registerUserRequest));
    }

}
