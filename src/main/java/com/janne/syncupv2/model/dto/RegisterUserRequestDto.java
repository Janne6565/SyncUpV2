package com.janne.syncupv2.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class RegisterUserRequestDto {
    @NotBlank(message = "Username is mandatory")
    @Size(min = 4, message = "Username should at least be 4 characters long")
    private String username;
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password should at least be 8 characters long")
    private String password;
    @NotBlank(message = "Username is mandatory")
    @Email(message = "Email should be valid")
    private String email;
}
