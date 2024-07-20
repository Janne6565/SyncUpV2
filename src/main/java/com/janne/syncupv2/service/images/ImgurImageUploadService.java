package com.janne.syncupv2.service.images;

import com.janne.syncupv2.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ImgurImageUploadService implements ImageUploadService {
    private final WebClient webClient;
    private final ImgurConfig imgurConfig;

    private BufferedImage scaleImage(BufferedImage inputImage, double scaleFactor) {
        int scaledWidth = (int) (inputImage.getWidth() * scaleFactor);
        int scaledHeight = (int) (inputImage.getHeight() * scaleFactor);
        BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());
        scaledImage.getGraphics().drawImage(inputImage.getScaledInstance(scaledWidth, scaledHeight, BufferedImage.SCALE_SMOOTH), 0, 0, null);

        return scaledImage;
    }

    public String uploadImage(BufferedImage image, double scaleFactor) {
        BufferedImage scaledImage = scaleImage(image, scaleFactor);

        byte[] imageData;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ImageIO.write(scaledImage, "png", byteArrayOutputStream);
            byteArrayOutputStream.flush();
            imageData = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error writing image stream", e);
        }

        String base64Image = Base64.getEncoder().encodeToString(imageData);

        LinkedMultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("image", base64Image);
        try {
            return webClient.post()
                    .uri(imgurConfig.getImgurUploadEndpoint())
                    .header("Authorization", "Client-ID " + imgurConfig.getClientId())
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(requestBody))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw RequestException.builder()
                    .message("image failed")
                    .errorObject(requestBody)
                    .status(501)
                    .build();
        }
    }

}
