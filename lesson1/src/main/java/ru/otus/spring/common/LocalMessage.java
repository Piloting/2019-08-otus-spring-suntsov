package ru.otus.spring.common;

public interface LocalMessage {
    String getMessage(String name, Object... args);
    String getMessage(String name);
}
