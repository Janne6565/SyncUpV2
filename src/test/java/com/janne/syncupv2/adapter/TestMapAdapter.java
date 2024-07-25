package com.janne.syncupv2.adapter;

import com.janne.syncupv2.model.dto.incomming.externalApi.valorantApi.maps.ValorantApiMapDto;
import com.janne.syncupv2.model.jpa.post.Map;
import com.janne.syncupv2.model.jpa.util.ScaledImage;
import com.janne.syncupv2.service.images.ImageUploadService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TestMapAdapter {

    @Mock
    private ImageUploadService imageUploadService;
    @InjectMocks
    private MapAdapter mapAdapter;

    private ScaledImage buildScaledImage(String url) {
        return ScaledImage.builder()
                .fullScaleUrl("fullScale://" + url)
                .thumbnailUrl("thumbnail://" + url)
                .build();
    }

    @SneakyThrows
    @Test
    public void givenValorantMapDto_verifyConvertedMap() {
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

        Mockito.when(imageUploadService.uploadScaledImages(Mockito.any(String.class))).thenAnswer(
                invocationOnMock -> buildScaledImage(invocationOnMock.getArgument(0))
        );

        Map convertedMap = mapAdapter.convertMap(mockValorantApiMapsDto);

        Map expectedMap = Map.builder()
                .name(mockValorantApiMapsDto.getDisplayName())
                .id(mockValorantApiMapsDto.getUuid())
                .listViewIcon(buildScaledImage(mockValorantApiMapsDto.getListViewIcon()))
                .displayIcon(buildScaledImage(mockValorantApiMapsDto.getDisplayIcon()))
                .stylizedImage(buildScaledImage(mockValorantApiMapsDto.getStylizedBackgroundImage()))
                .premierImage(buildScaledImage(mockValorantApiMapsDto.getPremierBackgroundImage()))
                .splashImage(buildScaledImage(mockValorantApiMapsDto.getSplash()))
                .listViewIconTall(buildScaledImage(mockValorantApiMapsDto.getListViewIconTall()))
                .build();

        assertEquals(convertedMap, expectedMap);

    }

}
