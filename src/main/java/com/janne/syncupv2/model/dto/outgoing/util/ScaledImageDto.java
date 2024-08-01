package com.janne.syncupv2.model.dto.outgoing.util;


import com.janne.syncupv2.service.images.ImageScaleFormat;
import lombok.Data;

@Data
public class ScaledImageDto {
    private String fullScaleUrl;
    private String thumbnailUrl;
    private ImageScaleFormat format;
}
