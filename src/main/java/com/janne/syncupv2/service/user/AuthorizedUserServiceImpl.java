package com.janne.syncupv2.service.user;

import com.janne.syncupv2.model.dto.outgoing.PrivateUserDto;

public interface AuthorizedUserServiceImpl {
    PrivateUserDto deleteUser();

    PrivateUserDto getCurrentUser();
}
