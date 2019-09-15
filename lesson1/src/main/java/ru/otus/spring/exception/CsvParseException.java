package ru.otus.spring.exception;

public class CsvParseException extends RuntimeException {
    public CsvParseException(String message, Throwable cause){
        super(message, cause);
    }
}
