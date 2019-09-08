package ru.otus.spring.service;

import org.apache.commons.lang3.StringUtils;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.QuestionOption;

import java.util.*;

/**
 * Сервис взаимодействия с пользоваелем
 */
public class CommunicationServiceConsoleImpl implements CommunicationService {

    @Override
    public Person getPerson() {
        Scanner sc = getScanner();
        System.out.print("Введите фамилию и имя: ");
        String answer = sc.next();
        String[] names = answer.split(" ");

        Person person = new Person();
        if (names.length > 0){
            person.setLastName(names[0]);
        }
        if (names.length > 1){
            person.setName(names[1]);
        }
        return person;
    }

    protected Scanner getScanner() {
        return new Scanner(System.in);
    }

    /**
     * Получить ответ на вопрос
     * @param question вопрос
     * @return ответ, номер элемента в answerOptions (с 0)
     */
    public Set<QuestionOption> getAnswer(Question question){
        // печать в консоль вопроса и вариантов ответа
        System.out.println(question.getQuestion());
        System.out.println("Варианты ответа:");
        int i = 1;

        // мапа [номер ответа - dto ответа]
        Map<Integer, QuestionOption> optionMap = new HashMap<>(question.getQuestionOptionList().size());

        // печать вариатнов отвта
        for (QuestionOption answerOption : question.getQuestionOptionList()) {
            optionMap.put(i, answerOption);
            System.out.println("\t" + i++ + ". " + answerOption.getText());
        }

        // получение ответа пользователя
        return getAnswerFromConsole(optionMap);
    }

    /**
     * Получение списка цифр из консоли.
     */
    private Set<QuestionOption> getAnswerFromConsole(Map<Integer, QuestionOption> optionMap){
        Scanner sc = getScanner();
        Set<QuestionOption> responseOptionSet = new HashSet<>();

        while (responseOptionSet.isEmpty()) {
            System.out.println("Введите ответ:");
            String complexAnswer = sc.next();

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
                System.out.println("Не удалось распознать введенный ответ. Попробуйте снова.");
                break;
            }

            int inputInt = Integer.parseInt(singleAnswer);
            if (!optionMap.containsKey(inputInt)){
                System.out.println("Введенное значение не является доступным вариантом. Попробуйте снова.");
                break;
            }

            responseOptionSet.add(optionMap.get(inputInt));
        }
    }
}
