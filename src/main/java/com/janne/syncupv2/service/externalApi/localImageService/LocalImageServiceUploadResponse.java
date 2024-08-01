package com.janne.syncupv2.service.externalApi.localImageService;

import lombok.Data;

@Data
public class LocalImageServiceUploadResponse {
    private String id;
    private String fullSizeUrl;
    private String thumbnailUrl;
    private String format;
}
