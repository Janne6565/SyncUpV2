package com.janne.syncupv2.model.dto.incomming.requests.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationUserRequest {

    private String email;
    private String password;
}
