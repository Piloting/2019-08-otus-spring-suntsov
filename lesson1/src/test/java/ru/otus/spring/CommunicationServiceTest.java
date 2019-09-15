package ru.otus.spring;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.spring.common.LocalMessage;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.QuestionOption;
import ru.otus.spring.service.ChannelService;
import ru.otus.spring.service.CommunicationService;
import ru.otus.spring.service.CommunicationServiceImpl;
import ru.otus.spring.service.ScannerChannelServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;

public class CommunicationServiceTest {

    @Test
    public void test(){
        String testAnswers = "0\nasd\n15m\n///\n 1,2\n";
        InputStream inputStream = new ByteArrayInputStream(testAnswers.getBytes(StandardCharsets.UTF_8));

        ChannelService channel = new ScannerChannelServiceImpl(new Scanner(inputStream));
        CommunicationService communicationService = new CommunicationServiceImpl(channel, new LocalMessage() {
            @Override public String getMessage(String name, Object... args) { return name; }
            @Override public String getMessage(String name) { return name; }
        });
        Question question = new Question();
        question.setQuestion("Is this the most important question?");

        question.setQuestionOptionList(Arrays.asList(
                QuestionOption.builder().isCorrect(true).text("Yes").build(),
                QuestionOption.builder().isCorrect(true).text("Sure").build(),
                QuestionOption.builder().isCorrect(false).text("No").build()
        ));

        Set<QuestionOption> optionList = communicationService.getAnswer(question);

        Assert.assertEquals("2 ответа", optionList.size(), 2);
    }
}
