package ru.otus.spring.common;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocalizationServiceImpl implements LocalizationService {

    private final MessageSource messageSource;
    private final Locale locale;

    public LocalizationServiceImpl(MessageSource messageSource, Locale locale)  {
        this.messageSource = messageSource;
        this.locale = locale;
    }

    public String getMessage(String name, Object... args){
        return messageSource.getMessage(name, args, locale);
    }

    @Override
    public String getMessage(String name) {
        return messageSource.getMessage(name, null, locale);
    }
}
