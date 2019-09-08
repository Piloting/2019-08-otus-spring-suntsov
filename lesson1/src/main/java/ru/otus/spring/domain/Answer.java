package ru.otus.spring.domain;

import lombok.Data;
import java.util.List;

/**
 * Полученный ответ
 */
@Data
public class Answer {
    /**
     * Исходный вопрос
     */
    private Question question;

    /**
     * Полученные варианты ответа
     */
    private List<QuestionOption> answerOptionList;

    public Integer calculateCorrectPercent(){
        Long countCorrectInQuestion = question.getQuestionOptionList().stream().filter(QuestionOption::isCorrect).count();
        Long countCorrectInAnswer = answerOptionList.stream().filter(QuestionOption::isCorrect).count();

        if (countCorrectInAnswer.equals(0L)){
            // ни одного правильного
            return 0;
        }

        // % правильных. XXX неправильные не учитываются
        long percent = countCorrectInAnswer / countCorrectInQuestion * 100;
        return (int) percent;
    }
}
