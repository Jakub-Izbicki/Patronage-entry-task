package com.izbicki.jakub.Message;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class ResourceBundleMessageSourceConfig {

    @Bean
    public ResourceBundleMessageSource exceptionMessageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("exceptions");
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }
}
