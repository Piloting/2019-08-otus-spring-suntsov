package ru.otus.spring.domain;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Результаты теста
 */
@Data
@ToString
public class TestResult {
    /**
     * Имя испытуемого
     */
    private Person person;

    /**
     * Название теста
     */
    private String testName;

    /**
     * Итоговый балл
     */
    private int value;

    /**
     * Ответы пользователя
     */
    private List<Answer> answerList;
}
