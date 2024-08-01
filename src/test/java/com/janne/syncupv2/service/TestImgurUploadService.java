package com.janne.syncupv2.service;

import com.janne.syncupv2.Application;
import com.janne.syncupv2.model.jpa.util.ScaledImage;
import com.janne.syncupv2.service.images.ImageUploadService;
import lombok.SneakyThrows;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;


@TestPropertySource(properties = "spring.task.scheduling.enabled=false")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = Application.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class TestImgurUploadService {

    private static final Logger log = LoggerFactory.getLogger(TestImgurUploadService.class);
    @Autowired
    private ImageUploadService imageUploadService;
    @Autowired
    private WebClient webClient;
    @Autowired
    private okhttp3.OkHttpClient okHttpClient;

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

    @SneakyThrows
    @Test
    public void givenImageUrlAndDeletion_expectImageAvailability() {
        String imageUrl = "https://projektejwkk.de/assets/SyncUp.png";

        ScaledImage uploadedImage = imageUploadService.uploadScaledImages(imageUrl);
        log.info(uploadedImage.toString());

        Assertions.assertTrue(doesImageExist(uploadedImage.getFullScaleUrl()));
        Assertions.assertTrue(doesImageExist(uploadedImage.getThumbnailUrl()));

        //imageUploadService.deleteImage(uploadedImage);

        Assertions.assertFalse(doesImageExist(uploadedImage.getFullScaleUrl()));
        Assertions.assertFalse(doesImageExist(uploadedImage.getThumbnailUrl()));
    }
}
