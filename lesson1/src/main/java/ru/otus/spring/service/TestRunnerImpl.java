package ru.otus.spring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.spring.common.LocalMessage;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.TestResult;

/**
 * Сервис прохождения теста
 */
@Service
public class TestRunnerImpl implements TestRunner {

    private final PersonService personService;
    private final TestService testService;
    private final Integer successPercent;
    private final LocalMessage localMessage;
    
    public TestRunnerImpl(PersonService personService,
                          TestService testService,
                          @Value("${successPercent}") String successPercentConfig, 
                          LocalMessage localMessage){
        this.personService = personService;
        this.testService = testService;
        this.successPercent = Integer.parseInt(successPercentConfig);
        this.localMessage = localMessage;
    }
    

    @Override
    public void runTest() {
        // запросить пользователя
        Person person = personService.getPerson();
        
        TestResult testResult = testService.processTest();

        System.out.println("-----------------");
        System.out.println(localMessage.getMessage("student", person.getFullName()));
        System.out.println(localMessage.getMessage("test_no", testResult.getTestName()));
        System.out.println(localMessage.getMessage("correct_answers",  testResult.getValue()));
        System.out.println(localMessage.getMessage(successPercent > testResult.getValue() ? "fail_exam" : "pass_exam"));
        
    }
}
