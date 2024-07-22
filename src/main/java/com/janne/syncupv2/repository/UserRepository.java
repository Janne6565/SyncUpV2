package com.janne.syncupv2.repository;

import com.janne.syncupv2.model.jpa.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Collection<User> searchAllByUsertag(String usertag);

    boolean existsByEmail(String email);

    long countByEmail(String email);
}
