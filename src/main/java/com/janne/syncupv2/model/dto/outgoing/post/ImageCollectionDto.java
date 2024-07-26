package com.janne.syncupv2.model.dto.outgoing.post;

import com.janne.syncupv2.model.dto.outgoing.util.ScaledImageDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageCollectionDto {
    private Long id;
    private ScaledImageDto imageLooking;
    private ScaledImageDto imageStanding;
    private ScaledImageDto imageLanding;
}
