package com.janne.syncupv2.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilConfig {
    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}
