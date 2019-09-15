package ru.otus.spring.domain;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import ru.otus.spring.dao.TextToQuestionOption;

import java.util.List;

/**
 * Вопрос
 */
@Data
public class Question {
    /**
     * ID
     */
    private Integer id;

    /**
     * Текст вопроса
     */
    @CsvBindByName(required = true)
    private String question;

    /**
     * Варианты ответа
     */
    @CsvBindAndSplitByName(column = "options", elementType=QuestionOption.class, splitOn = "\\|", converter=TextToQuestionOption.class)
    private List<QuestionOption> questionOptionList;
}
