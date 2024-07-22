package com.janne.syncupv2.service.user;

import com.janne.syncupv2.model.dto.outgoing.user.PrivateUserDto;

public interface AuthorizedUserServiceImpl {
    PrivateUserDto deleteUser();

    PrivateUserDto getCurrentUser();
}
