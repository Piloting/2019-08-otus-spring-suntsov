package ru.otus.spring.service;

import ru.otus.spring.domain.TestResult;

/**
 * Сервис прохождения теста
 */
public interface TestService {

    /**
     * Запуск теста
     * @return результат теста
     */
    TestResult processTest();
}
