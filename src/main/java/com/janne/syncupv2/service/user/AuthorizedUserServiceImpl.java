package com.janne.syncupv2.service.user;

import com.janne.syncupv2.model.dto.outgoing.PrivateUserDto;

public interface AuthorizedUserServiceImpl {
    public PrivateUserDto deleteUser();
    public PrivateUserDto getCurrentUser();
}
