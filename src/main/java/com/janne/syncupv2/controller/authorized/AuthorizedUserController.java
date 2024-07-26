package com.janne.syncupv2.controller.authorized;

import com.janne.syncupv2.model.dto.outgoing.user.PrivateUserDto;
import com.janne.syncupv2.service.user.AuthorizedUserServiceImplImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/authorized")
public class AuthorizedUserController {

    private final AuthorizedUserServiceImplImpl privateUserService;
    private final ModelMapper modelMapper;

    @DeleteMapping("/user")
    public ResponseEntity<PrivateUserDto> deleteUser() {
        return ResponseEntity.ok(modelMapper.map(privateUserService.deleteUser(), PrivateUserDto.class));
    }

    @GetMapping("/user")
    public ResponseEntity<PrivateUserDto> getUser() {
        return ResponseEntity.ok(modelMapper.map(privateUserService.getCurrentUser(), PrivateUserDto.class));
    }
}
