package ru.otus.spring.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import ru.otus.spring.domain.Question;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class QuestionDaoCsv implements QuestionDao {

    private final String questionFileName;

    public QuestionDaoCsv(String questionFileName){
        this.questionFileName = questionFileName;
    }

    public List<Question> getAllQuestions() {
        InputStream questionStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(questionFileName);
        // чтение csv спец утилитой сразу в dto. Question аннотирован для правильного маппинга
        return new CsvToBeanBuilder<Question>(new InputStreamReader(questionStream, UTF_8)).withType(Question.class).build().parse();
    }
}
