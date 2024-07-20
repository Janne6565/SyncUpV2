package com.janne.syncupv2.config;

import com.janne.syncupv2.service.images.ImageUploadService;
import com.janne.syncupv2.service.images.ImgurConfig;
import com.janne.syncupv2.service.images.ImgurImageUploadService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class UtilConfig {

    @Value("${imgur.clientId}")
    private String imgurClientId;

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
                .imgurUploadEndpoint("https://api.imgur.com/3/image")
                .clientId(imgurClientId)
                .build();
    }

    @Bean
    public ImageUploadService imageUploadService() {
        return new ImgurImageUploadService(webClient(), imgurConfig());
    }
}
