package com.janne.syncupv2.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.janne.syncupv2.model.dto.outgoing.util.SpotDto;
import com.janne.syncupv2.model.jpa.post.Spot;
import com.janne.syncupv2.service.externalApi.imgur.ImgurConfig;
import com.janne.syncupv2.service.externalApi.localImageService.LocalImageUploadService;
import com.janne.syncupv2.service.images.ImageUploadService;
import okhttp3.OkHttpClient;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class UtilConfig {

    @Value("${external.api.imgur.client-id}")
    private String imgurClientId;
    @Value("${external.api.imgur.endpoint}")
    private String imgurApiUrl;
    @Value("${external.api.imgur.thumbnail-postfix}")
    private String imgurThumbnailPostfix;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Spot, SpotDto>() {
            @Override
            protected void configure() {
                map().setMapId(source.getMap().getId());
            }
        });
        return modelMapper;
    }

    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }

    @Bean
    public ImgurConfig imgurConfig() {
        return ImgurConfig.builder()
                .imgurUploadEndpoint(imgurApiUrl)
                .clientId(imgurClientId)
                .thumbnailPostfix(imgurThumbnailPostfix)
                .build();
    }

    @Bean
    public ImageUploadService imageUploadService(LocalImageUploadService localImageUploadService) {
        return localImageUploadService;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient().newBuilder().build();
    }
}
