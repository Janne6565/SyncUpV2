package com.janne.syncupv2.model.dto.outgoing.post;

import com.janne.syncupv2.model.dto.outgoing.util.ScaledImageDto;
import lombok.Data;

@Data
public class ImageCollectionDto {
    private String id;
    private ScaledImageDto imageLooking;
    private ScaledImageDto imageStanding;
    private ScaledImageDto imageLanding;
}
