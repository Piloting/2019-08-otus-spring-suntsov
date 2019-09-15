package ru.otus.spring.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.spring.common.LocalMessage;
import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.CsvParseException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class QuestionDaoCsv implements QuestionDao {

    private final String questionFileName;
    private final LocalMessage localMessage;

    public QuestionDaoCsv(@Value("${question.fileName}") String questionFileName, LocalMessage localMessage){
        this.questionFileName = questionFileName;
        this.localMessage = localMessage;
    }

    public List<Question> getAllQuestions() {
        InputStream questionStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(questionFileName);
        try {
            // чтение csv спец утилитой сразу в dto. Question аннотирован для правильного маппинга
            return new CsvToBeanBuilder<Question>(new InputStreamReader(questionStream, UTF_8)).withType(Question.class).build().parse();
        } catch (Exception e){
            throw new CsvParseException(localMessage.getMessage("error_parse", questionFileName), e);
        }
    }
}
