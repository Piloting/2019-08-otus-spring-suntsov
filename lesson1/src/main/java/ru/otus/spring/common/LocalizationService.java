package ru.otus.spring.common;

public interface LocalizationService {
    String getMessage(String name, Object... args);
    String getMessage(String name);
}
