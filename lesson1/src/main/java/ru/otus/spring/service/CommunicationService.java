package ru.otus.spring.service;

import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.QuestionOption;

import java.util.Set;

/**
 * Сервис взаимодействия с пользоваелем
 */
public interface CommunicationService {

    /**
     * Получить пользователя
     */
    Person getPerson();

    /**
     * Получить ответ на вопрос
     * @param question вопрос
     * @return ответы
     */
    Set<QuestionOption> getAnswer(Question question);
}
