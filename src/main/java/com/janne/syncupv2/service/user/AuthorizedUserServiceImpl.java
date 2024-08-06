package com.janne.syncupv2.service.user;

import com.janne.syncupv2.exception.RequestException;
import com.janne.syncupv2.model.jpa.user.User;
import com.janne.syncupv2.repository.TokenRepository;
import com.janne.syncupv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizedUserServiceImpl implements AuthorizedUserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public User deleteUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        tokenRepository.deleteByUser(user);
        userRepository.deleteById(user.getId());
        return user;
    }

    @Override
    public User getCurrentUser() {
        if (SecurityContextHolder.getContext() == null || SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            throw RequestException.builder()
                    .message("User not authenticated")
                    .status(401)
                    .build();
        }
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> RequestException.builder()
                .message("User not found")
                .status(404)
                .build());
    }
}
