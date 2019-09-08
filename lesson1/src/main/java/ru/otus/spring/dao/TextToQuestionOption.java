package ru.otus.spring.dao;

import com.opencsv.bean.AbstractCsvConverter;
import ru.otus.spring.domain.QuestionOption;

public class TextToQuestionOption extends AbstractCsvConverter {
    private static final String IS_CORRECT = "1";

    public QuestionOption convertToRead(String value) {
        QuestionOption option = new QuestionOption();
        String[] split = value.split(";", 2);
        option.setCorrect(IS_CORRECT.equals(split[0]));
        option.setText(split[1]);
        return option;
    }
}