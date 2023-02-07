package com.ritrovo.userservice.configuration;

import com.ritrovo.userservice.converter.UserToDtoConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;

@Configuration
public class ConverterConfig {

    @Bean(name = "conversionService")
    public ConversionService getConversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        conversionService.addConverter(new UserToDtoConverter());
        return conversionService;
    }
}
