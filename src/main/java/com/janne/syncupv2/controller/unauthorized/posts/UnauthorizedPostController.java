package com.janne.syncupv2.controller.unauthorized.posts;

import com.janne.syncupv2.model.dto.outgoing.post.PostDto;
import com.janne.syncupv2.model.jpa.post.Post;
import com.janne.syncupv2.service.posts.UnauthorizedPostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/unauthorized/post")
@RequiredArgsConstructor
public class UnauthorizedPostController {

    private final UnauthorizedPostServiceImpl unauthorizedPostServiceImpl;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<PostDto[]> getPosts() {
        Post[] posts = unauthorizedPostServiceImpl.getPosts();
        return ResponseEntity.ok().body(modelMapper.map(posts, PostDto[].class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long id) {
        return ResponseEntity.ok().body(modelMapper.map(unauthorizedPostServiceImpl.getPost(id), PostDto.class));
    }

}
