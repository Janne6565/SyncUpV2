package com.janne.syncupv2.service.externalApi.imgur;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.janne.syncupv2.model.dto.incomming.externalApi.imgur.ImgurUploadResponse;
import com.janne.syncupv2.model.jpa.util.ScaledImage;
import com.janne.syncupv2.service.images.ImageUploadService;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(ImgurUploadService.class);
    private final ImgurConfig imgurConfig;
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;

    @Override
    public ScaledImage uploadScaledImages(String imageUrl) {
        ImgurUploadResponse uploadFullResolutionResult = uploadImageInternally(imageUrl);
        try {
            return generateScaledImageFromFullScaleUpload(uploadFullResolutionResult);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ScaledImage uploadScaledImages(BufferedImage image) {
        ImgurUploadResponse uploadFullResolutionResult = uploadImageInternally(image);
        try {
            return generateScaledImageFromFullScaleUpload(uploadFullResolutionResult);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteImage(ScaledImage image) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        sendRequest(imgurConfig.getImgurUploadEndpoint() + "/" + image.getDeleteThumbnailUrl(), RequestBody.create(JSON, "{}"), "DELETE");
        sendRequest(imgurConfig.getImgurUploadEndpoint() + "/" + image.getDeleteFullScaleToken(), RequestBody.create(JSON, "{}"), "DELETE");
    }

    private String buildThumbnailStringFromFullScale(String fullScaleUrl) {
        String[] splitUrl = fullScaleUrl.split("\\.");
        String extension = splitUrl[splitUrl.length - 1];
        String fullPath = Arrays.stream(splitUrl, 0, splitUrl.length - 1).collect(Collectors.joining("."));

        return fullPath + imgurConfig.getThumbnailPostfix() + "." + extension;
    }

    private boolean doesImageExist(String url) {
        Request checkForExistenceRequest = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();

        try (Response response = okHttpClient.newCall(checkForExistenceRequest).execute()) {
            assert response.code() == 200;
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private ScaledImage generateScaledImageFromFullScaleUpload(ImgurUploadResponse imgurUploadResponse) throws IOException {
        String fullScaleUrl = imgurUploadResponse.getData().getLink();
        ScaledImage scaledImage = ScaledImage.builder()
                .fullScaleUrl(fullScaleUrl)
                .deleteFullScaleToken(imgurUploadResponse.getData().getDeletehash())
                .build();

        String thumbnailUrl = buildThumbnailStringFromFullScale(fullScaleUrl);

        if (doesImageExist(thumbnailUrl)) {
            scaledImage.setThumbnailUrl(thumbnailUrl);
        } else {
            BufferedImage downscaledImage = scaleImage(urlToBufferedImage(fullScaleUrl), 0.3f);
            ImgurUploadResponse thumbnailUploadResponse = uploadImageInternally(downscaledImage);
            scaledImage.setThumbnailUrl(thumbnailUploadResponse.getData().getLink());
            scaledImage.setDeleteThumbnailUrl(thumbnailUploadResponse.getData().getDeletehash());
        }

        return scaledImage;
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

        return sendRequestBody(requestBody);
    }

    private ImgurUploadResponse uploadImageInternally(String url) {
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image", url)
                .addFormDataPart("type", "url")
                .addFormDataPart("title", "valorant_syncup_image_upload")
                .addFormDataPart("description", "valorant_syncup_image_upload")
                .build();

        return sendRequestBody(requestBody);
    }

    private ImgurUploadResponse sendRequestBody(RequestBody requestBody) {
        String response = sendRequest(imgurConfig.getImgurUploadEndpoint(), requestBody, "POST");
        try {
            ImgurUploadResponse imgurUploadResponse = objectMapper.readValue(response, ImgurUploadResponse.class);
            if (!imgurUploadResponse.isSuccess()) {
                throw new RuntimeException("Failed to upload Image using body: \n" + requestBody.toString());
            }
            return imgurUploadResponse;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String sendRequest(String url, RequestBody requestBody, String method) {
        Request request = new Request.Builder()
                .url(url)
                .method(method, requestBody)
                .addHeader("Authorization", "Client-ID " + imgurConfig.getClientId())
                .build();

        try {
            ImgurUploadResponse res;
            try (Response response = okHttpClient.newCall(request).execute()) {
                return Objects.requireNonNull(response.body()).string();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    public String uploadImage(BufferedImage bufferedImage) {
        return uploadImageInternally(bufferedImage).getData().getLink();
    }

    public String uploadImage(String imagePath) {
        return uploadImageInternally(imagePath).getData().getLink();
    }

}
