package com.janne.syncupv2.controller.unauthorized;

import com.janne.syncupv2.adapter.MapAdapter;
import com.janne.syncupv2.exception.RequestException;
import com.janne.syncupv2.model.dto.incomming.externalApi.valorantApi.maps.ValorantApiMapDto;
import com.janne.syncupv2.model.jpa.post.Map;
import com.janne.syncupv2.service.externalApi.valorantApi.ValorantApiService;
import com.janne.syncupv2.service.images.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final ImageUploadService imageUploadService;
    private final ValorantApiService valorantApiService;
    private final MapAdapter mapAdapter;

    @PostMapping("/uploadImage")
    public String uploadPicture(@RequestParam("image") MultipartFile image, @RequestParam double scale) {
        try {
            ByteArrayInputStream byteArrayOutputStream = new ByteArrayInputStream(image.getBytes());
            BufferedImage parsedImage = ImageIO.read(byteArrayOutputStream);
            return imageUploadService.uploadImage(parsedImage);
        } catch (IOException e) {
            throw RequestException.builder()
                    .errorObject(image)
                    .message("Cant parse file to image")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
        }
    }

    @GetMapping("/v1/unauthorized/maps")
    public ResponseEntity<Map> getMaps() {
        ValorantApiMapDto[] valorantApiMaps = valorantApiService.getMaps();
        try {
            Map map = mapAdapter.convertMap(valorantApiMaps[0]);
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            throw e;
        }
    }
}
