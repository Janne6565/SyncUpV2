package com.janne.syncupv2.repository;

import com.janne.syncupv2.model.jpa.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
