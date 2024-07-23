package com.janne.syncupv2.service.externalApi.imgur;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.janne.syncupv2.model.dto.incomming.externalApi.imgur.ImgurUploadResponse;
import com.janne.syncupv2.service.images.ImageUploadService;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImgurUploadService implements ImageUploadService {
    private final ImgurConfig imgurConfig;
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;

    public String uploadImage(BufferedImage bufferedImage) {
        // Convert BufferedImage to byte[]
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", baos);
            baos.flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert BufferedImage to byte[]", e);
        }
        byte[] imageBytes = baos.toByteArray();

        // Create the request body
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image", "image.png", RequestBody.create(imageBytes, MediaType.parse("image/png")))
                .addFormDataPart("type", "file")
                .addFormDataPart("title", "valorant_syncup_image_upload")
                .addFormDataPart("description", "valorant_syncup_image_upload")
                .build();

        // Build the request
        Request request = new Request.Builder()
                .url(imgurConfig.getImgurUploadEndpoint())
                .method("POST", requestBody)
                .addHeader("Authorization", "Client-ID " + imgurConfig.getClientId())
                .build();

        // Execute the request
        try {
            ImgurUploadResponse res;
            try (Response response = okHttpClient.newCall(request).execute()) {
                res = objectMapper.readValue(Objects.requireNonNull(response.body()).string(), ImgurUploadResponse.class);
            }
            return res.getData().getLink();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    public String uploadImage(String imagePath) {
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image", imagePath)
                .addFormDataPart("type", "url")
                .addFormDataPart("title", "valorant_syncup_image_upload")
                .addFormDataPart("description", "valorant_syncup_image_upload").build();

        Request request = new Request.Builder()
                .url(imgurConfig.getImgurUploadEndpoint())
                .method("POST", requestBody)
                .addHeader("Authorization", "Client-ID " + imgurConfig.getClientId())
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            ImgurUploadResponse res = objectMapper.convertValue(Objects.requireNonNull(response.body()).string(), ImgurUploadResponse.class);
            return res.getData().getLink();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
