package ru.otus.spring.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Locale;

@Service
public class LocalMessageImpl implements LocalMessage {

    private final MessageSource messageSource;
    private final Locale locale;

    public LocalMessageImpl(MessageSource messageSource, @Value("${locale}") String localeConfig)  {
        this.messageSource = messageSource;
        this.locale = StringUtils.parseLocale(localeConfig);
    }

    public String getMessage(String name, Object... args){
        return messageSource.getMessage(name, args, locale);
    }

    @Override
    public String getMessage(String name) {
        return messageSource.getMessage(name, null, locale);
    }
}
