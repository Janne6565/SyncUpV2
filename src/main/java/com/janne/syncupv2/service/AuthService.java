package com.janne.syncupv2.service;

import com.janne.syncupv2.exception.DuplicateEmailException;
import com.janne.syncupv2.model.dto.RegisterUserRequestDto;
import com.janne.syncupv2.model.user.User;
import com.janne.syncupv2.model.user.UserRole;
import com.janne.syncupv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String registerUser(RegisterUserRequestDto registerUserRequest) {
        if (userRepository.existsUserByEmail(registerUserRequest.getEmail())) {
            throw new DuplicateEmailException("Email already in use");
        }

        User user = User.builder()
                .email(registerUserRequest.getEmail().toLowerCase())
                .password(passwordEncoder.encode(registerUserRequest.getPassword()))
                .name(registerUserRequest.getName())
                .role(UserRole.USER)
                .build();

        userRepository.save(user);
        return "Success";
    }
}
