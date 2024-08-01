package com.janne.syncupv2.service.externalApi.localImageService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.janne.syncupv2.model.jpa.util.ScaledImage;
import com.janne.syncupv2.service.images.ImageScaleFormat;
import com.janne.syncupv2.service.images.ImageUploadService;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LocalImageUploadService implements ImageUploadService {

    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;

    @Value("${external.api.localImageService.endpoint}")
    private String localImageUploadServiceEndpoint;
    @Value("${external.api.localImageService.authToken}")
    private String authToken;
    @Value("${external.api.localImageService.hostedPrefix}")
    private String urlPrefix;

    @Override
    public String uploadImage(BufferedImage image, ImageScaleFormat imageScaleFormat) {
        return uploadImageInternally(image, imageScaleFormat).getFullSizeUrl();
    }

    private LocalImageServiceUploadResponse sendRequestBody(RequestBody requestBody) {
        Request request = new Request.Builder()
                .url(localImageUploadServiceEndpoint + "/image")
                .header("Authorization", "Bearer " + authToken)
                .post(requestBody)
                .build();
        try {
            String response = Objects.requireNonNull(okHttpClient.newCall(request).execute().body()).string();
            return objectMapper.readValue(response, LocalImageServiceUploadResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private LocalImageServiceUploadResponse uploadImageInternally(BufferedImage image, ImageScaleFormat imageScaleFormat) {
        image = resizeImage(image, imageScaleFormat.getWidth(), imageScaleFormat.getHeight());

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
                .addFormDataPart("format", imageScaleFormat.name())
                .build();

        return sendRequestBody(requestBody);
    }

    @Override
    public String uploadImage(String path, ImageScaleFormat imageScaleFormat) {
        try {
            return uploadImageInternally(urlToBufferedImage(path), imageScaleFormat).getFullSizeUrl();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getUploadServiceName() {
        return "localImageUploadService";
    }

    @Override
    public ScaledImage uploadScaledImages(String path, ImageScaleFormat imageScaleFormat) throws IOException {
        BufferedImage imageFromPath = urlToBufferedImage(path);
        return uploadScaledImages(imageFromPath, imageScaleFormat);
    }

    @Override
    public ScaledImage uploadScaledImages(BufferedImage image, ImageScaleFormat imageScaleFormat) {
        LocalImageServiceUploadResponse uploadResponse = uploadImageInternally(image, imageScaleFormat);
        return ScaledImage.builder()
                .deleteFullScaleToken(uploadResponse.getId())
                .thumbnailUrl(buildAbsolutePath(uploadResponse.getThumbnailUrl()))
                .fullScaleUrl(buildAbsolutePath(uploadResponse.getFullSizeUrl()))
                .uploadService(getUploadServiceName())
                .build();
    }

    @Override
    public ScaledImage uploadScaledImages(String path) {
        try {
            return uploadScaledImages(urlToBufferedImage(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ScaledImage uploadScaledImages(BufferedImage image) {
        return uploadScaledImages(image, ImageScaleFormat.detectScaleFromImage(image));
    }

    @Override
    public void deleteImage(ScaledImage image) {

    }

    private String buildAbsolutePath(String postfix) {
        return urlPrefix + postfix;
    }
}
