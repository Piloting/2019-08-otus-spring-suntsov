package ru.otus.spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.spring.common.LocalizationService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@DisplayName("Проверка сервиса взаимодейтвия")
public class CommunicationServiceTest {

    @DisplayName(" пройдена успешно")
    @Test
    public void test(){
        String testAnswers = "0\nasd\n15m\n///\n 1,2\n";
        InputStream inputStream = new ByteArrayInputStream(testAnswers.getBytes(StandardCharsets.UTF_8));

        LocalizationService localizationService = mock(LocalizationService.class);
        Mockito.when(localizationService.getMessage(any())).thenReturn("-mock-");
        
        ChannelService channel = new ScannerChannelServiceImpl(new Scanner(inputStream));
        CommunicationService communicationService = new CommunicationServiceImpl(channel, localizationService);
        Question question = new Question();
        question.setQuestion("Is this the most important question?");

        question.setQuestionOptionList(Arrays.asList(
                QuestionOption.builder().isCorrect(true).text("Yes").build(),
                QuestionOption.builder().isCorrect(true).text("Sure").build(),
                QuestionOption.builder().isCorrect(false).text("No").build()
        ));

        Set<QuestionOption> optionList = communicationService.getAnswer(question);

        Assertions.assertEquals(optionList.size(), 2, "2 ответа");
    }
}
