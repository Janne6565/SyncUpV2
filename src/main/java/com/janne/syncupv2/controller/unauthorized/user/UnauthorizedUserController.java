package com.janne.syncupv2.controller.unauthorized.user;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> user(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(unauthorizedUserService.getUser(userId));
    }
}
