package com.janne.syncupv2.model.dto.outgoing.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicUserDto {
    private String id;
    private String usertag;
    private String role;
}
