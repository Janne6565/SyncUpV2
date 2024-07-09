package com.janne.syncupv2.controller.demo;

import com.janne.syncupv2.model.dto.RegisterUserRequestDto;
import com.janne.syncupv2.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/demo")
@RequiredArgsConstructor
public class DemoController {

    private final AuthService authService;

    @PostMapping("/createUser")
    public ResponseEntity<String> createUser(@Valid @RequestBody RegisterUserRequestDto registerUserRequest) {
        return ResponseEntity.ok(authService.registerUser(registerUserRequest));
    }

}
