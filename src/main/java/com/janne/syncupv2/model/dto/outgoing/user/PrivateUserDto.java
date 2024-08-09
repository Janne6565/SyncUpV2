package com.janne.syncupv2.model.dto.outgoing.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivateUserDto {
    private String id;
    private String usertag;
    private String email;
    private String role;
}
