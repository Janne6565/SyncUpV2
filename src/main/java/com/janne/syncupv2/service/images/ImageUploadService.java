package com.janne.syncupv2.service.images;

import com.janne.syncupv2.model.jpa.util.ScaledImage;

import java.awt.image.BufferedImage;

public interface ImageUploadService {

    String uploadImage(BufferedImage image, double scaling);
    ScaledImage uploadScaledImages(BufferedImage image);
}
