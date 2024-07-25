package com.janne.syncupv2.service.maps;

import com.janne.syncupv2.adapter.MapAdapter;
import com.janne.syncupv2.model.dto.incomming.externalApi.valorantApi.maps.ValorantApiMapDto;
import com.janne.syncupv2.model.jpa.post.Map;
import com.janne.syncupv2.service.externalApi.valorantApi.ValorantApiService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class MapLoaderService {

    private static final Logger log = LoggerFactory.getLogger(MapLoaderService.class);
    private final ValorantApiService valorantApiService;
    private final MapService mapService;
    private final MapAdapter mapAdapter;

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.HOURS)
    public void loadMapsFromValorantApi() {
        log.debug("fetching new Maps");
        ValorantApiMapDto[] valorantApiMapDtos = valorantApiService.getMaps();
        System.out.println(Arrays.toString(valorantApiMapDtos));
        for (ValorantApiMapDto mapDto : valorantApiMapDtos) {
            if (!mapService.doesMapExist(mapDto.getUuid()) && !mapDto.getDisplayName().equals("The Range")) {
                log.info("New Map found: " + mapDto.getDisplayName() + " loading from dto: " + mapDto);
                Map map = mapService.saveMap(mapAdapter.convertMap(mapDto));
                log.info("Map saved as: " + map);
            }
        }
    }
}
