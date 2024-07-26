package com.janne.syncupv2.adapter;

import com.janne.syncupv2.model.dto.incomming.externalApi.valorantApi.maps.ValorantApiMapDto;
import com.janne.syncupv2.model.jpa.post.Map;
import com.janne.syncupv2.model.jpa.util.ScaledImage;
import com.janne.syncupv2.service.images.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MapAdapter {

    private final ImageUploadService imageUploadService;

    private ScaledImage uploadImage(String path) {
        if (path == null) {
            return null;
        }
        try {
            return imageUploadService.uploadScaledImages(path);
        } catch (IOException e) {
            return null;
        }
    }

    public Map convertMap(ValorantApiMapDto valorantApiMapDto) {
        try {
            return Map.builder()
                    .id(valorantApiMapDto.getUuid())
                    .name(valorantApiMapDto.getDisplayName())
                    .displayIcon(uploadImage(valorantApiMapDto.getDisplayIcon()))
                    .listViewIcon(uploadImage(valorantApiMapDto.getListViewIcon()))
                    .stylizedImage(uploadImage(valorantApiMapDto.getStylizedBackgroundImage()))
                    .splashImage(uploadImage(valorantApiMapDto.getSplash()))
                    .premierImage(uploadImage(valorantApiMapDto.getPremierBackgroundImage()))
                    .listViewIconTall(uploadImage(valorantApiMapDto.getListViewIconTall()))
                    .build();
        } catch (Error e) {
            throw new RuntimeException(e);
        }
    }

}
