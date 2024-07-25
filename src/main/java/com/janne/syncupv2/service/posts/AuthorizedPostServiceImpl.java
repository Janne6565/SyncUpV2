package com.janne.syncupv2.service.posts;


import com.janne.syncupv2.exception.RequestException;
import com.janne.syncupv2.model.jpa.post.ImageCollection;
import com.janne.syncupv2.model.jpa.post.Post;
import com.janne.syncupv2.model.jpa.post.Spot;
import com.janne.syncupv2.model.jpa.user.Permission;
import com.janne.syncupv2.model.jpa.user.User;
import com.janne.syncupv2.model.jpa.util.ScaledImage;
import com.janne.syncupv2.repository.PostRepository;
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
                        .imageStanding(imageUploadService.uploadScaledImages(imageStanding))
                        .imageLanding(imageUploadService.uploadScaledImages(imageLanding))
                        .imageLooking(imageUploadService.uploadScaledImages(imageLooking))
                        .build()
                ).build();
        return postRepository.save(post);
    }

    public void deletePost(User authenticatedUser, Post post) {
        if (authenticatedUser.getId() != post.getUser().getId() && !authenticatedUser.getRole().getPermissions().contains(Permission.ADMIN_DELETE)) {
            throw RequestException.builder()
                    .errorObject(post)
                    .message("Unable to delete post which isn't your own")
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .build();
        }

        postRepository.delete(post);
        for (ScaledImage image : post.getImageCollection().toArray()) {
            imageUploadService.deleteImage(image);
        }
    }
}
