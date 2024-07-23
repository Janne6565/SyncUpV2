package com.janne.syncupv2.service.externalApi.imgur;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.janne.syncupv2.model.dto.incomming.externalApi.imgur.ImgurUploadResponse;
import com.janne.syncupv2.model.jpa.util.ScaledImage;
import com.janne.syncupv2.service.images.ImageUploadService;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImgurUploadService implements ImageUploadService {
    private final ImgurConfig imgurConfig;
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;

    @Override
    public ScaledImage uploadScaledImages(BufferedImage image) {
        ImgurUploadResponse uploadFullResolutionResult = uploadImageInternally(image);
        String fullScaleUrl = uploadFullResolutionResult.getData().getLink();

        String[] splitUrl = fullScaleUrl.split("\\.");
        String extension = splitUrl[splitUrl.length - 1];
        String fullPath = Arrays.stream(splitUrl, 0, splitUrl.length - 1).collect(Collectors.joining("."));

        String thumbnailUrl = fullPath.toString() + imgurConfig.getThumbnailPostfix() + "." + extension;

        Request checkForExistenceRequest = new Request.Builder()
                .url(thumbnailUrl)
                .method("GET", null)
                .build();
        try (Response response = okHttpClient.newCall(checkForExistenceRequest).execute()) {
            assert response.code() == 200;
        } catch (IOException e) {
            thumbnailUrl = uploadImage(scaleImage(image, 0.3f));
        }

        return ScaledImage.builder()
                .fullScaleUrl(fullScaleUrl)
                .thumbnailUrl(thumbnailUrl)
                .build();
    }

    @Override
    public ScaledImage uploadScaledImages(String path) throws IOException {
        return uploadScaledImages(urlToBufferedImage(path));
    }

    private ImgurUploadResponse uploadImageInternally(BufferedImage image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", byteArrayOutputStream);
            byteArrayOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert BufferedImage to byte[]", e);
        }
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image", "image.png", RequestBody.create(imageBytes, MediaType.parse("image/png")))
                .addFormDataPart("type", "file")
                .addFormDataPart("title", "valorant_syncup_image_upload")
                .addFormDataPart("description", "valorant_syncup_image_upload")
                .build();

        Request request = new Request.Builder()
                .url(imgurConfig.getImgurUploadEndpoint())
                .method("POST", requestBody)
                .addHeader("Authorization", "Client-ID " + imgurConfig.getClientId())
                .build();

        try {
            ImgurUploadResponse res;
            try (Response response = okHttpClient.newCall(request).execute()) {
                res = objectMapper.readValue(Objects.requireNonNull(response.body()).string(), ImgurUploadResponse.class);
            }
            return res;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    public String uploadImage(BufferedImage bufferedImage) {
        return uploadImageInternally(bufferedImage).getData().getLink();
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
