package com.janne.syncupv2.repository;

import com.janne.syncupv2.model.jpa.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, String> {
    @Query("SELECT p FROM Post p WHERE " +
            "(LOWER(p.user.usertag) LIKE CONCAT('%', LOWER(:username), '%') OR :username IS NULL) AND " +
            "(LOWER(p.title) LIKE CONCAT('%', LOWER(:title), '%') OR :title IS NULL) AND " +
            "(:map IS NULL OR p.from.map.id = :map) AND" +
            "(:userId IS NULL OR p.user.id = :userId)")
    List<Post> findPostByCriteria(String username, String userId, String title, String map);
}
