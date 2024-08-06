package com.janne.syncupv2.service.posts;


import com.janne.syncupv2.exception.RequestException;
import com.janne.syncupv2.model.jpa.post.ImageCollection;
import com.janne.syncupv2.model.jpa.post.Post;
import com.janne.syncupv2.model.jpa.post.Spot;
import com.janne.syncupv2.model.jpa.user.Permission;
import com.janne.syncupv2.model.jpa.user.User;
import com.janne.syncupv2.model.jpa.util.ScaledImage;
import com.janne.syncupv2.repository.PostRepository;
import com.janne.syncupv2.service.images.ImageScaleFormat;
import com.janne.syncupv2.service.images.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
@RequiredArgsConstructor
public class AuthorizedPostServiceImpl {

    private final PostRepository postRepository;
    private final ImageUploadService imageUploadService;

    public Post createPost(User user, Spot from, Spot to, String title, BufferedImage imageStanding, BufferedImage imageLanding, BufferedImage imageLooking) {
        Post post = Post.builder()
                .user(user)
                .from(from)
                .to(to)
                .title(title)
                .imageCollection(ImageCollection.builder()
                        .imageStanding(imageUploadService.uploadScaledImages(imageStanding, ImageScaleFormat.FULL_SCREEN))
                        .imageLanding(imageUploadService.uploadScaledImages(imageLanding, ImageScaleFormat.FULL_SCREEN))
                        .imageLooking(imageUploadService.uploadScaledImages(imageLooking, ImageScaleFormat.FULL_SCREEN))
                        .build()
                ).build();
        return postRepository.save(post);
    }

    public Post updatePost(User authenticatedUser, String postId, String title, Spot from, Spot to) {
        Post post = postRepository.findById(postId).orElseThrow(() -> RequestException.builder()
                .message("Post not found")
                .status(HttpStatus.NOT_FOUND.value())
                .build());

        post.setTitle(title);
        post.setFrom(from);
        post.setTo(to);
        return postRepository.save(post);
    }

    public void deletePost(User authenticatedUser, String postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> RequestException.builder()
                .message("Post not found")
                .status(HttpStatus.NOT_FOUND.value())
                .build());

        assertPostOwnership(authenticatedUser, post);

        postRepository.delete(post);
        for (ScaledImage image : post.getImageCollection().toArray()) {
            imageUploadService.deleteImage(image);
        }
    }

    private void assertPostOwnership(User authenticatedUser, Post post) {
        if (authenticatedUser.getId() != post.getUser().getId() && !authenticatedUser.getRole().getPermissions().contains(Permission.ADMIN_UPDATE)) {
            throw RequestException.builder()
                    .errorObject(post)
                    .message("Unable to update post which isn't your own")
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .build();
        }
    }
}
