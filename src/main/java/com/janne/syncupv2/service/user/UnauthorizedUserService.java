package com.janne.syncupv2.service.user;

import com.janne.syncupv2.model.dto.outgoing.PublicUserDto;

public interface UnauthorizedUserService {
    public PublicUserDto getUser(Integer id);
}
