package com.janne.syncupv2.model.dto.outgoing.map;

import com.janne.syncupv2.model.dto.outgoing.util.ScaledImageDto;
import lombok.Data;

@Data
public class MapDto {
    private String id;
    private String name;
    private ScaledImageDto displayIcon;
    private ScaledImageDto listViewIcon;
    private ScaledImageDto listViewIconTall;
    private ScaledImageDto splashImage;
    private ScaledImageDto stylizedImage;
    private ScaledImageDto premierImage;
}
