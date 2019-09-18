package ru.otus.spring.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Locale;

@Getter
@Service
public class LocalProperties {
    
    private final Locale locale;
    private final String questionBaseFileName;
    
    public LocalProperties(@Value("${locale}") Locale locale, @Value("${question.baseFileName}") String questionBaseFileName){
        this.locale = locale;
        this.questionBaseFileName = questionBaseFileName;
    }

    public String getLocalCsvFile() {
        String postfix = locale.getLanguage();
        int i = questionBaseFileName.lastIndexOf(".");
        String fileName = questionBaseFileName.substring(0, i) + "_" + postfix + questionBaseFileName.substring(i);

        URL questionUrl = Thread.currentThread().getContextClassLoader().getResource(fileName);
        if (questionUrl == null){
            fileName = questionBaseFileName;
        }
        
        return fileName;
    }
    
}
