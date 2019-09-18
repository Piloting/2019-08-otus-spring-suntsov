package ru.otus.spring;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.spring.common.LocalProperties;
import ru.otus.spring.dao.QuestionDaoCsv;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.QuestionOption;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class QuestionDaoTest {

    @Test
    public void csvLoadTest(){
        QuestionDaoCsv dao = new QuestionDaoCsv(new LocalProperties(Locale.ENGLISH, "questionsTest.csv"));
        List<Question> allQuestions = dao.getAllQuestions();

        Assert.assertEquals("Должно быть 3 элемента", allQuestions.size(), 3);
        System.out.println(allQuestions);
        List<Question> result = allQuestions.stream().filter(line -> line.getQuestion().startsWith("To be")).collect(Collectors.toList());

        Assert.assertEquals("Не должно быть повторений." + result.toString(), result.size(), 1);
        Question question = result.iterator().next();
        Assert.assertEquals("Должно быть 2 опции", question.getQuestionOptionList().size(), 2);
        Assert.assertTrue("Из 2 опций, одна правильная", question.getQuestionOptionList().stream().anyMatch(QuestionOption::isCorrect));
        Assert.assertTrue("Из 2 опций, одна не правильная", question.getQuestionOptionList().stream().anyMatch(questionOption -> !questionOption.isCorrect()));
    }
}
