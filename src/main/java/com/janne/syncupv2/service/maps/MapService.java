package com.janne.syncupv2.service.maps;

import com.janne.syncupv2.exception.RequestException;
import com.janne.syncupv2.model.jpa.post.Map;
import com.janne.syncupv2.repository.MapRepository;
import com.janne.syncupv2.service.images.ImageScaleFormat;
import com.janne.syncupv2.service.images.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@RequiredArgsConstructor
@Service
public class MapService {

    private final MapRepository mapRepository;
    private final ImageUploadService imageUploadService;

    public Map[] getAllMaps() {
        return mapRepository.findAll().toArray(new Map[0]);
    }

    public Map getMapById(String id) {
        if (!doesMapExist(id)) {
            throw RequestException.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("No map with given ID found")
                    .errorObject(id)
                    .build();
        }
        return mapRepository.findById(id).orElse(null);
    }

    public Map saveMap(Map map) {
        return mapRepository.save(map);
    }

    public Map createNewMap(String name, BufferedImage displayIcon, BufferedImage listViewIcon, BufferedImage listViewIconTall, BufferedImage splashImage, BufferedImage stylizedImage, BufferedImage premierImage) {
        Map map = Map.builder()
                .name(name)
                .displayIcon(imageUploadService.uploadScaledImages(displayIcon, ImageScaleFormat.FULL_SCREEN))
                .listViewIcon(imageUploadService.uploadScaledImages(listViewIcon, ImageScaleFormat.FULL_SCREEN))
                .listViewIconTall(imageUploadService.uploadScaledImages(listViewIconTall, ImageScaleFormat.FULL_SCREEN))
                .splashImage(imageUploadService.uploadScaledImages(splashImage, ImageScaleFormat.FULL_SCREEN))
                .stylizedImage(imageUploadService.uploadScaledImages(stylizedImage, ImageScaleFormat.FULL_SCREEN))
                .premierImage(imageUploadService.uploadScaledImages(premierImage, ImageScaleFormat.FULL_SCREEN))
                .build();

        return mapRepository.save(map);
    }

    public boolean doesMapExist(String id) {
        return mapRepository.existsById(id);
    }
}
