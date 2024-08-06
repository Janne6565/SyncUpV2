package com.janne.syncupv2.controller.unauthorized.posts;

import com.janne.syncupv2.model.dto.outgoing.post.PostDto;
import com.janne.syncupv2.model.jpa.post.Post;
import com.janne.syncupv2.service.posts.UnauthorizedPostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/unauthorized/post")
@RequiredArgsConstructor
public class UnauthorizedPostController {

    private final UnauthorizedPostServiceImpl unauthorizedPostServiceImpl;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<PostDto[]> getPosts(@RequestParam(required = false) String username, @RequestParam(required = false) String userId, @RequestParam(required = false) String title, @RequestParam(required = false) String map) {
        Post[] posts = unauthorizedPostServiceImpl.getPosts(username, userId, title, map);
        return ResponseEntity.ok().body(modelMapper.map(posts, PostDto[].class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable String id) {
        return ResponseEntity.ok().body(modelMapper.map(unauthorizedPostServiceImpl.getPost(id), PostDto.class));
    }

}
