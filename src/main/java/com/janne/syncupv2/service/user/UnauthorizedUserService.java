package com.janne.syncupv2.service.user;

import com.janne.syncupv2.exception.RequestException;
import com.janne.syncupv2.model.dto.outgoing.user.PublicUserDto;

public interface UnauthorizedUserService {
    PublicUserDto getUser(Integer id) throws RequestException;
}
