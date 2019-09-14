package ru.otus.spring.service;

import org.apache.commons.lang3.StringUtils;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.QuestionOption;

import java.util.*;

/**
 * Сервис взаимодействия с пользоваелем
 */
public class CommunicationServiceImpl implements CommunicationService {

    private final ChannelService channel;

    public CommunicationServiceImpl(ChannelService channel){
        this.channel = channel;
    }

    /**
     * Получить ответ на вопрос
     * @param question вопрос
     * @return ответ, номер элемента в answerOptions (с 0)
     */
    public Set<QuestionOption> getAnswer(Question question){
        // печать в консоль вопроса и вариантов ответа
        channel.say(question.getQuestion());
        channel.say("Варианты ответа:");
        int i = 1;

        // мапа [номер ответа - dto ответа]
        Map<Integer, QuestionOption> optionMap = new HashMap<>(question.getQuestionOptionList().size());

        // печать вариатнов отвта
        for (QuestionOption answerOption : question.getQuestionOptionList()) {
            optionMap.put(i, answerOption);
            String string = "\t" + i++ + ". " + answerOption.getText();
            channel.say(string);
        }

        // получение ответа пользователя
        return getAnswerFromConsole(optionMap);
    }

    /**
     * Получение списка цифр из консоли.
     */
    private Set<QuestionOption> getAnswerFromConsole(Map<Integer, QuestionOption> optionMap){
        Set<QuestionOption> responseOptionSet = new HashSet<>();

        while (responseOptionSet.isEmpty()) {
            channel.say("Введите ответ:");
            String complexAnswer = channel.listen();

            if (StringUtils.isNotBlank(complexAnswer)){
                parseUserAnswer(optionMap, responseOptionSet, complexAnswer);
            }
        }

        return responseOptionSet;
    }

    /**
     * Обработка введенных данных
     */
    private void parseUserAnswer(Map<Integer, QuestionOption> optionMap, Set<QuestionOption> responseOptionSet, String complexAnswer) {
        for (String singleAnswer : complexAnswer.split(",")) {
            if (StringUtils.isBlank(singleAnswer)){
                continue;
            }

            if (!StringUtils.isNumeric(singleAnswer.trim())){
                channel.say("Не удалось распознать введенный ответ. Попробуйте снова.");
                break;
            }

            int inputInt = Integer.parseInt(singleAnswer);
            if (!optionMap.containsKey(inputInt)){
                channel.say("Введенное значение не является доступным вариантом. Попробуйте снова.");
                break;
            }

            responseOptionSet.add(optionMap.get(inputInt));
        }
    }
}
