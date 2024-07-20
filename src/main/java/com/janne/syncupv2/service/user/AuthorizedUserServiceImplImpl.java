package com.janne.syncupv2.service.user;

import com.janne.syncupv2.model.dto.outgoing.PrivateUserDto;
import com.janne.syncupv2.model.user.User;
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

    public PrivateUserDto deleteUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        tokenRepository.deleteByUser(user);
        userRepository.deleteById(user.getId());
        return modelMapper.map(user, PrivateUserDto.class);
    }

    @Override
    public PrivateUserDto getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        return modelMapper.map(user, PrivateUserDto.class);
    }
}
