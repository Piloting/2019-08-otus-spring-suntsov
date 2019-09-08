package ru.otus.spring.service;

import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Сервис прохождения теста
 */
public class TestServiceImpl implements TestService {

    private final QuestionDao questionDao;
    private final CommunicationService communicationService;

    public TestServiceImpl(QuestionDao questionDao, CommunicationService communicationService){
        this.questionDao = questionDao;
        this.communicationService = communicationService;
    }


    /**
     * Запуск теста
     * @return результат теста
     */
    public TestResult processTest(){

        // получение списка вопросов
        List<Question> allQuestions = questionDao.getAllQuestions();

        TestResult result = new TestResult();
        result.setTestName("Тест №1");

        // запросить пользователя
        Person person = communicationService.getPerson();
        result.setPerson(person);

        // получаем ответ на каждый вопрос
        List<Answer> answerList = new ArrayList<>(allQuestions.size());
        for (Question question : allQuestions) {
            Answer answer = new Answer();
            answer.setQuestion(question);
            answer.getAnswerOptionList().addAll(communicationService.getAnswer(question));
            answerList.add(answer);
        }

        // подсчет итоговой оценки
        result.setValue(calculateResultValue(answerList));

        return result;
    }

    private Integer calculateResultValue(List<Answer> answerList) {
        OptionalDouble average = answerList.stream().mapToInt(Answer::calculateCorrectPercent).average();
        double resultPercent = average.orElseGet(() -> 0);
        return (int) resultPercent;
    }
}
