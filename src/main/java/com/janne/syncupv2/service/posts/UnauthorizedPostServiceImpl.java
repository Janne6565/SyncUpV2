package com.janne.syncupv2.service.posts;

import com.janne.syncupv2.model.jpa.post.Post;
import com.janne.syncupv2.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnauthorizedPostServiceImpl {

    private final PostRepository postRepository;

    public Post[] getPosts(String username, String userId, String title, String map) {
        return postRepository.findPostByCriteria(username, userId, title, map).toArray(Post[]::new);
    }

    public Post getPost(String id) {
        return postRepository.findById(id).orElseThrow();
    }
}
