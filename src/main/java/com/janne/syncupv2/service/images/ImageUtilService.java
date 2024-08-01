package com.janne.syncupv2.service.images;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class ImageUtilService {

    public BufferedImage loadImageFromMultipartFile(MultipartFile image) {
        try {
            ByteArrayInputStream byteArrayOutputStream = new ByteArrayInputStream(image.getBytes());
            return ImageIO.read(byteArrayOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
