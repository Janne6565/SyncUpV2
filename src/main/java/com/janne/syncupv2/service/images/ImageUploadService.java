package com.janne.syncupv2.service.images;

import com.janne.syncupv2.model.jpa.util.ScaledImage;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

public interface ImageUploadService {

    String uploadImage(BufferedImage image);
    String uploadImage(String path);
    void deleteImage(ScaledImage image);

    default BufferedImage scaleImage(BufferedImage image, float scale) {
        int newWidth = (int) (image.getWidth() * scale);
        int newHeight = (int) (image.getHeight() * scale);
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_4BYTE_ABGR);
        resizedImage.getGraphics().drawImage(image, 0, 0, newWidth, newHeight, null);
        return resizedImage;
    }

    default ScaledImage uploadScaledImages(BufferedImage image) {
        return ScaledImage.builder()
                .fullScaleUrl(uploadImage(scaleImage(image, 1f)))
                .thumbnailUrl(uploadImage(scaleImage(image, .3f)))
                .build();
    }

    default ScaledImage uploadScaledImages(String path) throws IOException {
        return uploadScaledImages(urlToBufferedImage(path));
    }

    default BufferedImage urlToBufferedImage(@NotNull String urlPath) throws IOException {
        URL url = URI.create(urlPath).toURL();
        BufferedImage image = ImageIO.read(url);
        if (image == null) {
            throw new IOException("Couldnt decode image: " + urlPath);
        }
        return image;
    }
}
