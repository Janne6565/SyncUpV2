package com.janne.syncupv2.service;

import com.janne.syncupv2.controller.auth.RegisterUserRequest;
import com.janne.syncupv2.model.user.User;
import com.janne.syncupv2.model.user.UserRole;
import com.janne.syncupv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String registerUser(RegisterUserRequest registerUserRequest) {
        Optional<User> emailInUse = userRepository.findByEmail(registerUserRequest.getEmail());
        if (emailInUse.isPresent()) {
            throw new DuplicateKeyException("Email already in use");
        }

        User user = User.builder()
                .email(registerUserRequest.getEmail())
                .password(passwordEncoder.encode(registerUserRequest.getPassword()))
                .username(registerUserRequest.getUsername())
                .role(UserRole.USER)
                .build();

        userRepository.save(user);
        return "Success";
    }
}
