package com.janne.syncupv2.service.user;

import com.janne.syncupv2.model.dto.outgoing.PublicUserDto;

public interface UnauthorizedUserService {
    PublicUserDto getUser(Integer id);
}
