package ru.otus.spring;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ru.otus.spring.dao.QuestionDaoCsv;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.QuestionOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionDaoTest {

    public static final String CONST_BIT = "Быть";

    @Ignore
    @Test
    public void csvLoadTest(){
        QuestionDaoCsv dao = new QuestionDaoCsv("questionsTest.csv");
        List<Question> allQuestions = dao.getAllQuestions();

        Assert.assertEquals("Должно быть 3 элемента", allQuestions.size(), 3);
        System.out.println(allQuestions);
        //List<Question> result = allQuestions.stream().filter(line -> line.getQuestion().startsWith("Быть")).collect(Collectors.toList());
        List<Question> result = new ArrayList<>();
        for (Question question : allQuestions) {
            System.out.println(question.getQuestion() + " " + CONST_BIT + " " + question.getQuestion().startsWith(CONST_BIT));
            if (question.getQuestion().startsWith(CONST_BIT)) {
                result.add(question);
            }
        }

        Assert.assertEquals("Не должно быть повторений." + result.toString(), result.size(), 1);
        Question question = result.iterator().next();
        Assert.assertEquals("Должно быть 2 опции", question.getQuestionOptionList().size(), 2);
        Assert.assertTrue("Из 2 опций, одна правильная", question.getQuestionOptionList().stream().anyMatch(QuestionOption::isCorrect));
        Assert.assertTrue("Из 2 опций, одна не правильная", question.getQuestionOptionList().stream().anyMatch(questionOption -> !questionOption.isCorrect()));
    }
}
