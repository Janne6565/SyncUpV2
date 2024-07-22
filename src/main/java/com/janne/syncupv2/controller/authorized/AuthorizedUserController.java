package com.janne.syncupv2.controller.authorized;

import com.janne.syncupv2.model.dto.outgoing.user.PrivateUserDto;
import com.janne.syncupv2.service.user.AuthorizedUserServiceImplImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthorizedUserController implements AuthorizedController {

    private final AuthorizedUserServiceImplImpl privateUserService;

    @DeleteMapping("/user")
    public ResponseEntity<PrivateUserDto> deleteUser() {
        return ResponseEntity.ok(privateUserService.deleteUser());
    }

    @GetMapping("/user")
    public ResponseEntity<PrivateUserDto> getUser() {
        return ResponseEntity.ok(privateUserService.getCurrentUser());
    }
}
