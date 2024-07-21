package com.janne.syncupv2.controller.unauthorized.user;

import com.janne.syncupv2.model.dto.outgoing.user.PublicUserDto;
import com.janne.syncupv2.service.user.UnauthorizedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/unauthorized/user")
public class UnauthorizedUserController {

    private final UnauthorizedUserService unauthorizedUserService;

    @GetMapping("/{userId}")
    public ResponseEntity<PublicUserDto> user(@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(unauthorizedUserService.getUser(userId));
    }
}
