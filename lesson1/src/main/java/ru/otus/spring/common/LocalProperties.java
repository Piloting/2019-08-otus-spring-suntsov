package ru.otus.spring.common;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Locale;

@Setter
@Component
@ConfigurationProperties(prefix = "question")
public class LocalProperties {
    
    private String locale;
    private String baseFileName;

    public String getLocalCsvFile() {
        String postfix = getLocale().getLanguage();
        int i = baseFileName.lastIndexOf(".");
        String fileName = baseFileName.substring(0, i) + "_" + postfix + baseFileName.substring(i);

        URL questionUrl = Thread.currentThread().getContextClassLoader().getResource(fileName);
        if (questionUrl == null){
            fileName = baseFileName;
        }
        
        return fileName;
    }
    
    public Locale getLocale(){
        return Locale.forLanguageTag(this.locale);
    }
}
