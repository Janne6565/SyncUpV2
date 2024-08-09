package com.janne.syncupv2.service.user;

import com.janne.syncupv2.model.jpa.user.User;

public interface AuthorizedUserService {
    User deleteUser();

    User getCurrentUser();
}
