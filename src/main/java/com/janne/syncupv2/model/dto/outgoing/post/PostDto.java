package com.janne.syncupv2.model.dto.outgoing.post;

import com.janne.syncupv2.model.dto.outgoing.user.PublicUserDto;
import com.janne.syncupv2.model.dto.outgoing.util.SpotDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private String id;
    private String title;
    private PublicUserDto user;
    private SpotDto from;
    private SpotDto to;
    private ImageCollectionDto imageCollection;
}
