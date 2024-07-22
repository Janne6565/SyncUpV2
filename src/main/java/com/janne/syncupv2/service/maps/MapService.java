package com.janne.syncupv2.service.maps;

import com.janne.syncupv2.model.jpa.post.Map;
import com.janne.syncupv2.repository.MapRepository;
import com.janne.syncupv2.service.images.ImageUploadService;
import lombok.RequiredArgsConstructor;
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

    public Map getMapById(Float id) {
        return mapRepository.findById(id).orElse(null);
    }

    public Map saveMap(Map map) {
        return mapRepository.save(map);
    }

    public Map createNewMap(String name, BufferedImage displayIcon, BufferedImage listViewIcon, BufferedImage listViewIconTall, BufferedImage splashImage, BufferedImage stylizedImage, BufferedImage premierImage) {
        Map map = Map.builder()
                .name(name)
                .displayIcon(imageUploadService.uploadScaledImages(displayIcon))
                .listViewIcon(imageUploadService.uploadScaledImages(listViewIcon))
                .listViewIconTall(imageUploadService.uploadScaledImages(listViewIconTall))
                .splashImage(imageUploadService.uploadScaledImages(splashImage))
                .stylizedImage(imageUploadService.uploadScaledImages(stylizedImage))
                .premierImage(imageUploadService.uploadScaledImages(premierImage))
                .build();

        return mapRepository.save(map);
    }
}
