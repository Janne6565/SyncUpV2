package com.janne.syncupv2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.janne.syncupv2.auth.token.Token;
import com.janne.syncupv2.auth.token.TokenType;
import com.janne.syncupv2.exception.DuplicateEmailException;
import com.janne.syncupv2.exception.RequestException;
import com.janne.syncupv2.model.dto.outgoing.AuthenticationResponse;
import com.janne.syncupv2.model.dto.încomming.AuthenticationUserRequest;
import com.janne.syncupv2.model.dto.încomming.RegisterUserRequest;
import com.janne.syncupv2.model.user.User;
import com.janne.syncupv2.model.user.UserRole;
import com.janne.syncupv2.repository.TokenRepository;
import com.janne.syncupv2.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements LogoutHandler {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse registerUser(final RegisterUserRequest request) {
        if (0 < userRepository.countByEmail(request.getEmail().toLowerCase())) {
            throw new DuplicateEmailException(request.getEmail());
        }

        final User user = User.builder()
                .usertag(request.getUsertag())
                .email(request.getEmail().toLowerCase())
                .password(this.passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .build();
        User savedUser = this.userRepository.save(user);
        String jwtToken = this.jwtService.generateToken(user);
        String refreshToken = this.jwtService.generateRefreshToken(user);
        this.saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    public AuthenticationResponse authenticate(final AuthenticationUserRequest request) {
        try {
            this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail().toLowerCase(),
                            request.getPassword()
                    )
            );
        } catch (final BadCredentialsException e) {
            throw RequestException.builder()
                    .errorObject("Bad Credentials")
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("No matching user found")
                    .build();
        }
        final User user = this.userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow();
        final String jwtToken = this.jwtService.generateToken(user);
        final String refreshToken = this.jwtService.generateRefreshToken(user);
        this.revokeAllUserTokens(user);
        this.saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    private void saveUserToken(final User user, final String jwtToken) {
        final Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        this.tokenRepository.save(token);
    }

    private void revokeAllUserTokens(final User user) {
        final Collection<Token> validUserTokens = this.tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        this.tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken;
        String userEmail;
        if (null == authHeader || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = this.jwtService.extractUsername(refreshToken);
        if (null != userEmail) {
            final var user = userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (this.jwtService.isTokenValid(refreshToken, user)) {
                final var accessToken = this.jwtService.generateToken(user);
                this.revokeAllUserTokens(user);
                this.saveUserToken(user, accessToken);
                final var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    @Override
    public void logout(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        String jwt;
        if (null == authHeader || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        final var storedToken = this.tokenRepository.findByToken(jwt)
                .orElse(null);
        if (null != storedToken) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            this.tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }
    }
}