package com.janne.syncupv2.service;


import com.janne.syncupv2.adapter.MapAdapter;
import com.janne.syncupv2.model.dto.incomming.externalApi.valorantApi.maps.ValorantApiMapDto;
import com.janne.syncupv2.model.jpa.post.Map;
import com.janne.syncupv2.model.jpa.util.ScaledImage;
import com.janne.syncupv2.service.externalApi.valorantApi.ValorantApiService;
import com.janne.syncupv2.service.maps.MapLoaderService;
import com.janne.syncupv2.service.maps.MapService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;


@TestPropertySource(properties = "spring.task.scheduling.enabled=false")
@ExtendWith(MockitoExtension.class)
public class TestMapLoadingService {

    @Mock
    private ValorantApiService valorantApiService;

    @Mock
    private MapService mapService;

    @InjectMocks
    private MapLoaderService mapLoaderService;

    @Mock
    private MapAdapter mapAdapter;


    private final String fullScaleImagePrefix = "full_scale_url://";
    private final String thumbnailScaleImagePrefix = "thumbnail_scale_url://";

    private ScaledImage buildTestScaledImage(String imageUrl) {
        return ScaledImage.builder()
                .fullScaleUrl(fullScaleImagePrefix + imageUrl)
                .thumbnailUrl(thumbnailScaleImagePrefix + imageUrl)
                .build();
    }

    @SneakyThrows
    @Test
    public void givenExternalMaps_testCorrectMapLoading() {
        ValorantApiMapDto mockValorantApiMapsDto = ValorantApiMapDto.builder()
                .uuid("test-map-uuid")
                .displayName("Test Map")
                .listViewIcon("listviewiconimage")
                .displayIcon("displayiconimage")
                .stylizedBackgroundImage("stylizedbackgroundimage")
                .premierBackgroundImage("premierbackgroundimage")
                .splash("splashbackgroundimage")
                .listViewIconTall("listviewicontallimage")
                .build();

        when(valorantApiService.getMaps()).thenReturn(new ValorantApiMapDto[]{mockValorantApiMapsDto});
        List<Map> savedMaps = new ArrayList<>();
        when(mapService.saveMap(Mockito.any(Map.class))).thenAnswer(invocationOnMock -> {
            Map mapToSave = invocationOnMock.getArgument(0);
            savedMaps.add(mapToSave);
            return mapToSave;
        });

        when(mapAdapter.convertMap(Mockito.any(ValorantApiMapDto.class))).thenAnswer(invocationOnMock -> {
            ValorantApiMapDto valorantApiMapDto = invocationOnMock.getArgument(0);
            return Map.builder()
                    .displayIcon(buildTestScaledImage(valorantApiMapDto.getDisplayIcon()))
                    .listViewIconTall(buildTestScaledImage(valorantApiMapDto.getListViewIconTall()))
                    .premierImage(buildTestScaledImage(valorantApiMapDto.getPremierBackgroundImage()))
                    .splashImage(buildTestScaledImage(valorantApiMapDto.getSplash()))
                    .stylizedImage(buildTestScaledImage(valorantApiMapDto.getStylizedBackgroundImage()))
                    .listViewIcon(buildTestScaledImage(valorantApiMapDto.getListViewIcon()))
                    .name(valorantApiMapDto.getDisplayName())
                    .id(valorantApiMapDto.getUuid())
                    .build();
        });

        mapLoaderService.loadMapsFromValorantApi();

        Assertions.assertEquals(savedMaps.size(), 1);
        Map uploadedMap = savedMaps.getFirst();
        Assertions.assertNotNull(uploadedMap);

        Map resExpected = Map.builder()
                .id(mockValorantApiMapsDto.getUuid())
                .name(mockValorantApiMapsDto.getDisplayName())
                .listViewIcon(buildTestScaledImage(mockValorantApiMapsDto.getListViewIcon()))
                .splashImage(buildTestScaledImage(mockValorantApiMapsDto.getSplash()))
                .listViewIconTall(buildTestScaledImage(mockValorantApiMapsDto.getListViewIconTall()))
                .displayIcon(buildTestScaledImage(mockValorantApiMapsDto.getDisplayIcon()))
                .stylizedImage(buildTestScaledImage(mockValorantApiMapsDto.getStylizedBackgroundImage()))
                .premierImage(buildTestScaledImage(mockValorantApiMapsDto.getPremierBackgroundImage()))
                .build();

        Assertions.assertEquals(uploadedMap, resExpected);

    }

}