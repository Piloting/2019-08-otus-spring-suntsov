package ru.otus.spring.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class Config {

    @Bean(name = "messageSource")
    public static ReloadableResourceBundleMessageSource bundleConfig(){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("/localization/bundle");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
