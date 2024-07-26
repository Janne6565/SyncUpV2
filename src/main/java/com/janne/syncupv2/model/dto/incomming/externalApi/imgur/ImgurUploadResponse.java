package com.janne.syncupv2.model.dto.incomming.externalApi.imgur;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImgurUploadResponse {
    private int status;
    private boolean success;
    private DataInfo data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataInfo {
        private String id;
        private String deletehash;
        private String title;
        private String description;
        private String name;
        private String type;
        private int width;
        private int height;
        private int size;
        private int views;
        private String section;
        private String vote;
        private String nsfw;
        private String link;
        private long datetime;

        @JsonProperty("account_id")
        private String accountId;

        @JsonProperty("account_url")
        private String accountUrl;

        @JsonProperty("ad_type")
        private String adType;

        @JsonProperty("ad_url")
        private String adUrl;

        @JsonProperty("in_gallery")
        private boolean inGallery;

        @JsonProperty("in_most_viral")
        private boolean inMostViral;

        @JsonProperty("has_sound")
        private boolean hasSound;

        @JsonProperty("is_ad")
        private boolean isAd;

        private String[] tags;
        private int bandwidth;
        private boolean animated;
        private boolean favorite;
        private String mp4;
        private String hls;
    }
}
