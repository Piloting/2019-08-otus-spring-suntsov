package ru.otus.spring.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;
import ru.otus.spring.common.LocalProperties;
import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.CsvParseException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class QuestionDaoCsv implements QuestionDao {

    private final LocalProperties localeProperties;

    public QuestionDaoCsv(LocalProperties localeProperties){
        this.localeProperties = localeProperties;
    }

    public List<Question> getAllQuestions() {
        String fileName = localeProperties.getLocalCsvFile();
        try {
            InputStream questionStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            // чтение csv спец утилитой сразу в dto. Question аннотирован для правильного маппинга
            return new CsvToBeanBuilder<Question>(new InputStreamReader(questionStream, UTF_8)).withType(Question.class).build().parse();
        } catch (Exception e){
            throw new CsvParseException("Error processing csv file with questions: " + fileName, e);
        }
    }
}
