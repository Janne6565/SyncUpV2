package com.janne.syncupv2.controller.unauthorized;

import com.janne.syncupv2.model.dto.outgoing.PublicUserDto;
import com.janne.syncupv2.service.user.UnauthorizedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UnauthorizedUserController implements UnauthorizedController {

    private final UnauthorizedUserService unauthorizedUserService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<PublicUserDto> user(@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(unauthorizedUserService.getUser(userId));
    }
}
