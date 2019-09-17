package ru.otus.spring.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.otus.spring.common.LocalizationService;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.QuestionOption;

import java.util.*;

/**
 * Сервис взаимодействия с пользоваелем
 */
@Service
public class CommunicationServiceImpl implements CommunicationService {

    private final ChannelService channel;
    private final LocalizationService localizationService;

    public CommunicationServiceImpl(ChannelService channel, LocalizationService localizationService){
        this.channel = channel;
        this.localizationService = localizationService;
    }

    /**
     * Получить ответ на вопрос
     * @param question вопрос
     * @return ответ, номер элемента в answerOptions (с 0)
     */
    public Set<QuestionOption> getAnswer(Question question){
        // печать в консоль вопроса и вариантов ответа
        channel.say(question.getQuestion());
        channel.say(localizationService.getMessage("answer_option"));
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
            channel.say(localizationService.getMessage("say_answer"));
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
                channel.say(localizationService.getMessage("error_answer"));
                break;
            }

            int inputInt = Integer.parseInt(singleAnswer.trim());
            if (!optionMap.containsKey(inputInt)){
                channel.say(localizationService.getMessage("out_of_option"));
                break;
            }

            responseOptionSet.add(optionMap.get(inputInt));
        }
    }
}
