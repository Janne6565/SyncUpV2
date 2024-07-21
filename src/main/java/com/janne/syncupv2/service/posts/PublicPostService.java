package com.janne.syncupv2.service.posts;

import com.janne.syncupv2.model.jpa.post.Post;

import java.util.List;

public interface PublicPostService {
    List<Post> getPublicPosts();
    Post getPublicPostById(Long id);
    Post createPublicPost(Post post);
    Post updatePublicPost(Post post);
    void deletePublicPost(Long id);
}
