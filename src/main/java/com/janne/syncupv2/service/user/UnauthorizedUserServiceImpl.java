package com.janne.syncupv2.service.user;

import com.janne.syncupv2.exception.RequestException;
import com.janne.syncupv2.model.dto.outgoing.user.PublicUserDto;
import com.janne.syncupv2.model.jpa.user.User;
import com.janne.syncupv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UnauthorizedUserServiceImpl implements UnauthorizedUserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public PublicUserDto getUser(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw RequestException.builder()
                    .message("No user with id " + id + " found")
                    .status(HttpStatus.NOT_FOUND.value())
                    .errorObject(id)
                    .build();
        }
        return modelMapper.map(user, PublicUserDto.class);
    }
}
