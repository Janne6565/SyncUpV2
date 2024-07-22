package com.janne.syncupv2.controller.auth;

import com.janne.syncupv2.controller.V1ApiController;
import com.janne.syncupv2.model.dto.outgoing.AuthenticationResponse;
import com.janne.syncupv2.model.dto.incomming.requests.auth.AuthenticationUserRequest;
import com.janne.syncupv2.model.dto.incomming.requests.auth.RegisterUserRequest;
import com.janne.syncupv2.service.auth.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AuthenticationController extends V1ApiController {

    private final AuthenticationService authenticationService;

    @PostMapping("/auth/user")
    public ResponseEntity<AuthenticationResponse> createUser(
            @Valid @RequestBody RegisterUserRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.registerUser(request));
    }

    @GetMapping("/auth/user")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationUserRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/auth/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }

}
