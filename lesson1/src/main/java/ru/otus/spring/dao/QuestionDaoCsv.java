package ru.otus.spring.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.CsvParseException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class QuestionDaoCsv implements QuestionDao {

    private final String questionBaseFileName;
    private final Locale locale;

    public QuestionDaoCsv(@Value("${question.baseFileName}") String questionBaseFileName, Locale locale){
        this.questionBaseFileName = questionBaseFileName;
        this.locale = locale;
    }

    public List<Question> getAllQuestions() {
        String fileName = getLocalCsvFile(); 

        InputStream questionStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        
        if (questionStream == null){
            questionStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(questionBaseFileName);
            fileName = questionBaseFileName;
        }
        
        try {
            // чтение csv спец утилитой сразу в dto. Question аннотирован для правильного маппинга
            return new CsvToBeanBuilder<Question>(new InputStreamReader(questionStream, UTF_8)).withType(Question.class).build().parse();
        } catch (Exception e){
            throw new CsvParseException("Error processing csv file with questions: " + fileName, e);
        }
    }

    private String getLocalCsvFile() {
        String postfix = locale.getLanguage();
        int i = questionBaseFileName.lastIndexOf(".");
        return questionBaseFileName.substring(0, i) + "_" + postfix + questionBaseFileName.substring(i);
    }
}
