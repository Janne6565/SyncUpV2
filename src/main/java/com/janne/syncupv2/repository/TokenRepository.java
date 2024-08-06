package com.janne.syncupv2.repository;

import com.janne.syncupv2.auth.token.Token;
import com.janne.syncupv2.model.jpa.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query(value = """
            SELECT t FROM Token t INNER JOIN User u\s
            ON t.user.id = u.id\s
            WHERE u.id = :id and (t.expired = false and t.revoked = false)\s
            """)
    List<Token> findAllValidTokenByUser(String id);

    Optional<Token> findByToken(String token);

    @Transactional
    @Modifying
    @Query("delete from Token t where t.user = ?1")
    void deleteByUser(User user);
}
