package com.janne.syncupv2.controller.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest {
    private String username;
    private String password;
    private String email;
}
