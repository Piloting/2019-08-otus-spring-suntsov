package ru.otus.spring.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import ru.otus.spring.domain.Question;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class QuestionDaoCsv implements QuestionDao {

    private final String questionFileName;

    public QuestionDaoCsv(String questionFileName){
        this.questionFileName = questionFileName;
    }

    public List<Question> getAllQuestions() {
        InputStream questionStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(questionFileName);
        return new CsvToBeanBuilder<Question>(new InputStreamReader(questionStream)).withType(Question.class).build().parse();
    }
}
