package com.janne.syncupv2.model.dto.incomming.externalApi.valorantApi.maps;

import lombok.Data;

import java.util.List;

@Data
public class ValorantApiMapDto {
    private String uuid;
    private String displayName;
    private String narrativeDescription;
    private String tacticalDescription;
    private String coordinates;
    private String displayIcon;
    private String listViewIcon;
    private String listViewIconTall;
    private String splash;
    private String stylizedBackgroundImage;
    private String premierBackgroundImage;
    private String assetPath;
    private String mapUrl;
    private double xMultiplier;
    private double yMultiplier;
    private double xScalarToAdd;
    private double yScalarToAdd;
    private List<Callout> callouts;

    @Data
    public static class Callout {
        private String regionName;
        private String superRegionName;
        private MapLocation location;
    }

    @Data
    public static class MapLocation {
        private double x;
        private double y;
    }

}
