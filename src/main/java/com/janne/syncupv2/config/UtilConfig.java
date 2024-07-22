package com.janne.syncupv2.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.janne.syncupv2.service.externalApi.ImgurConfig;
import com.janne.syncupv2.service.externalApi.ImgurUploadService;
import com.janne.syncupv2.service.images.ImageUploadService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class UtilConfig {

    @Value("${external.api.imgur.endpoint}")
    private String imgurClientId;
    @Value("${external.api.imgur.client-id}")
    private String imgurApiUrl;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
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
                .build();
    }

    @Bean
    public ImageUploadService imageUploadService() {
        return new ImgurUploadService(webClient(), imgurConfig());
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
