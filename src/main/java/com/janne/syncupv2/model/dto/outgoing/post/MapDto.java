package com.janne.syncupv2.model.dto.outgoing.post;


import com.janne.syncupv2.model.dto.outgoing.util.ScaledImageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
