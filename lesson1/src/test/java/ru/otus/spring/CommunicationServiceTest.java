package ru.otus.spring;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.QuestionOption;
import ru.otus.spring.service.CommunicationService;
import ru.otus.spring.service.CommunicationServiceConsoleImpl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;

public class CommunicationServiceTest {

    @Ignore
    @Test
    public void test(){
        String testAnswers = "0 ,asd, ,15m, ///, 1,2";
        InputStream inputStream = new ByteArrayInputStream(testAnswers.getBytes(StandardCharsets.UTF_8));
        CommunicationService communicationService = new CommunicationServiceConsoleImpl(){
            protected Scanner getScanner() {
                return new Scanner(inputStream);
            }
        };
        Question question = new Question();
        question.setQuestion("Это самый важный вопрос?");

        question.setQuestionOptionList(Arrays.asList(
                QuestionOption.builder().isCorrect(true).text("Да").build(),
                QuestionOption.builder().isCorrect(true).text("Конечно").build(),
                QuestionOption.builder().isCorrect(false).text("Нет").build()
        ));

        Set<QuestionOption> optionList = communicationService.getAnswer(question);

        Assert.assertEquals("2 ответа", optionList.size(), 2);
    }
}
