package com.janne.syncupv2.model.dto.încomming.imgur;

import lombok.Data;

@Data
public class ImgurUploadResponse {
    private String id;
    private int width;
    private int height;
    private String link;
}
