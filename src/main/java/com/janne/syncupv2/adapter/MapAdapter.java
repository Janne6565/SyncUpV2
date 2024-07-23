package com.janne.syncupv2.adapter;

import com.janne.syncupv2.model.dto.incomming.externalApi.valorantApi.maps.ValorantApiMapDto;
import com.janne.syncupv2.model.jpa.post.Map;
import com.janne.syncupv2.service.images.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MapAdapter {

    private final ImageUploadService imageUploadService;

    public Map convertMap(ValorantApiMapDto valorantApiMapDto) {
        try {
            return Map.builder()
                    .id(valorantApiMapDto.getUuid())
                    .name(valorantApiMapDto.getDisplayName())
                    .displayIcon(imageUploadService.uploadScaledImages(valorantApiMapDto.getDisplayIcon()))
                    .listViewIcon(imageUploadService.uploadScaledImages(valorantApiMapDto.getListViewIcon()))
                    .stylizedImage(imageUploadService.uploadScaledImages(valorantApiMapDto.getStylizedBackgroundImage()))
                    .splashImage(imageUploadService.uploadScaledImages(valorantApiMapDto.getSplash()))
                    .premierImage(imageUploadService.uploadScaledImages(valorantApiMapDto.getPremierBackgroundImage()))
                    .listViewIconTall(imageUploadService.uploadScaledImages(valorantApiMapDto.getListViewIconTall()))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
