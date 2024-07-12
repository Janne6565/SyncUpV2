package com.janne.syncupv2.repository;

import com.janne.syncupv2.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Collection<User> searchAllByName(String username);

    boolean existsByEmail(String email);
}