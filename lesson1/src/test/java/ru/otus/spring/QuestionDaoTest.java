package ru.otus.spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.common.LocalProperties;
import ru.otus.spring.dao.QuestionDaoCsv;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.QuestionOption;

import java.util.List;
import java.util.stream.Collectors;

@DisplayName("Загрузка csv файла")
@SpringBootTest
public class QuestionDaoTest {

    @MockBean
    LocalProperties localProperties;

    @DisplayName(" проходит успешно")
    @Test
    public void csvLoadTest(){
        Mockito.when(localProperties.getLocalCsvFile()).thenReturn("questionsTest.csv");
        
        QuestionDaoCsv dao = new QuestionDaoCsv(localProperties);
        List<Question> allQuestions = dao.getAllQuestions();

        Assertions.assertEquals(allQuestions.size(), 3, "Должно быть 3 элемента");
        System.out.println(allQuestions);
        List<Question> result = allQuestions.stream().filter(line -> line.getQuestion().startsWith("To be")).collect(Collectors.toList());

        Assertions.assertEquals(result.size(), 1, "Не должно быть повторений." + result.toString());
        Question question = result.iterator().next();
        Assertions.assertEquals(question.getQuestionOptionList().size(), 2, "Должно быть 2 опции");
        Assertions.assertTrue(question.getQuestionOptionList().stream().anyMatch(QuestionOption::isCorrect), "Из 2 опций, одна правильная");
        Assertions.assertTrue(question.getQuestionOptionList().stream().anyMatch(questionOption -> !questionOption.isCorrect()), "Из 2 опций, одна не правильная");
    }
}
