package com.janne.syncupv2.model.dto.outgoing.map;

import com.janne.syncupv2.model.jpa.util.ScaledImage;
import lombok.Data;

@Data
public class MapDto {
    private String id;
    private String name;
    private ScaledImage displayIcon;
    private ScaledImage listViewIcon;
    private ScaledImage listViewIconTall;
    private ScaledImage splashImage;
    private ScaledImage stylizedImage;
    private ScaledImage premierImage;
}
