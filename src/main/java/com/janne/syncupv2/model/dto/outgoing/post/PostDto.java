package com.janne.syncupv2.model.dto.outgoing.post;

import com.janne.syncupv2.model.dto.outgoing.user.PublicUserDto;
import com.janne.syncupv2.model.jpa.post.Spot;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostDto {
    private Long id;
    private String title;
    private PublicUserDto user;
    private Spot from;
    private Spot to;
    private ImageCollectionDto imageCollection;
}
