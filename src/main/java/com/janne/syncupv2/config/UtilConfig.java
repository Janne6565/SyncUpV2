package com.janne.syncupv2.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.janne.syncupv2.service.externalApi.imgur.ImgurConfig;
import okhttp3.OkHttpClient;
import org.modelmapper.ModelMapper;
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
