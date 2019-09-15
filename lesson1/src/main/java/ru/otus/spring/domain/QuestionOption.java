package ru.otus.spring.domain;

import lombok.*;

/**
 * Варианты ответа на вопрос
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class QuestionOption {

    /**
     * ID
     */
    private Integer id;

    /**
     * Текст варианта ответа
     */
    private String text;

    /**
     * Признак верного ответа
     */
    private boolean isCorrect;
}
