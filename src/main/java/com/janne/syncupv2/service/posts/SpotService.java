package com.janne.syncupv2.service.posts;

import com.janne.syncupv2.model.jpa.post.Spot;
import com.janne.syncupv2.repository.SpotRepository;
import com.janne.syncupv2.service.maps.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpotService {

    private final SpotRepository spotRepository;
    private final MapService mapService;

    public Spot createSpot(float x, float y, String mapId) {
        Spot spot = Spot.builder()
                .x(x)
                .y(y)
                .map(mapService.getMapById(mapId))
                .build();

        return spotRepository.save(spot);
    }

    public Spot getSpot(Long id) {
        return spotRepository.findById(id).orElseThrow();
    }

    public Spot[] getSpots() {
        return spotRepository.findAll().toArray(new Spot[0]);
    }

}
