package com.janne.syncupv2.model.dto.outgoing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.management.relation.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicUserDto {
    private Integer id;
    private String usertag;
    private String role;
}
