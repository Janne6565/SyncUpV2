package com.janne.syncupv2.model.dto.outgoing.post;


import com.janne.syncupv2.model.dto.outgoing.util.ImageDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MapDto {
    private long id;
    private String name;
    private ImageDto displayIcon;
    private ImageDto listViewIcon;
    private ImageDto listViewIconTall;
    private ImageDto splashImage;
    private ImageDto stylizedImage;
    private ImageDto premierImage;
}
