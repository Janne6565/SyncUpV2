package com.janne.syncupv2.service.user;

import com.janne.syncupv2.model.jpa.user.User;
import com.janne.syncupv2.repository.TokenRepository;
import com.janne.syncupv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizedUserServiceImplImpl implements AuthorizedUserServiceImpl {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
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
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
    }
}
