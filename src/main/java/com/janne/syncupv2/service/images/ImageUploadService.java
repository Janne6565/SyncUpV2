package com.janne.syncupv2.service.images;

import java.awt.image.BufferedImage;

public interface ImageUploadService {

    String uploadImage(BufferedImage image, double scaling);
}
