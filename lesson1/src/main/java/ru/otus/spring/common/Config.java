package ru.otus.spring.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.StringUtils;

import java.util.Locale;

@Configuration
public class Config {

    @Bean(name = "messageSource")
    public static ReloadableResourceBundleMessageSource bundleConfig(){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("/localization/bundle");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
    
    @Bean()
    public static Locale getLocale(@Value("${locale}") String localeConfig){
        return StringUtils.parseLocale(localeConfig);
    }
}
