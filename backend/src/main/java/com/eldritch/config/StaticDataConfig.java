package com.eldritch.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class StaticDataConfig {
    @Bean
    public MessageSource staticData() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:static_data"); // Base name of the properties files
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
