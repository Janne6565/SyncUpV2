package com.janne.syncupv2.controller.authorized;

import com.janne.syncupv2.exception.RequestException;
import com.janne.syncupv2.model.dto.outgoing.post.PostDto;
import com.janne.syncupv2.model.jpa.post.Spot;
import com.janne.syncupv2.model.jpa.user.User;
import com.janne.syncupv2.service.images.ImageUtilService;
import com.janne.syncupv2.service.posts.AuthorizedPostServiceImpl;
import com.janne.syncupv2.service.posts.SpotService;
import com.janne.syncupv2.service.user.AuthorizedUserServiceImplImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequestMapping("/v1/authorized/post")
@RequiredArgsConstructor
public class AuthorizedPostController {

    private final AuthorizedPostServiceImpl authorizedPostServiceImpl;
    private final AuthorizedUserServiceImplImpl authorizedUserServiceImpl;
    private final ImageUtilService imageUtilService;
    private final ModelMapper modelMapper;
    private final SpotService spotService;

    @PostMapping("/")
    public ResponseEntity<PostDto> createPost(String title, long fromSpot, long toSpot, MultipartFile imageStanding, MultipartFile imageLanding, MultipartFile imageLooking) {
        User user = authorizedUserServiceImpl.getCurrentUser();
        Spot from = spotService.getSpot(fromSpot);
        Spot to = spotService.getSpot(toSpot);
        if (!Objects.equals(from.getMap().getId(), to.getMap().getId())) {
            throw RequestException.builder()
                    .errorObject(new Spot[]{from, to})
                    .message("Spots are not on the same map")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
        }

        return ResponseEntity.ok().body(modelMapper.map(authorizedPostServiceImpl.createPost(
                user,
                spotService.getSpot(fromSpot),
                spotService.getSpot(toSpot),
                title,
                imageUtilService.loadImageFromMultipartFile(imageStanding),
                imageUtilService.loadImageFromMultipartFile(imageLanding),
                imageUtilService.loadImageFromMultipartFile(imageLooking)
        ), PostDto.class));
    }
}
