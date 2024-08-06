package com.janne.syncupv2.repository;

import com.janne.syncupv2.model.jpa.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, String> {
    @Query("SELECT p FROM Post p WHERE " +
            "(:username IS NULL OR LOWER(p.user.usertag) LIKE LOWER(CONCAT('%', :username, '%'))) AND " +
            "(:title IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:map IS NULL OR p.from.map.id = :map)")
    List<Post> findPostByCriteria(String username, String title, String map);
}
