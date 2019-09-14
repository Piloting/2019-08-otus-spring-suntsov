package ru.otus.spring.service;

import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.TestResult;

/**
 * Сервис прохождения теста
 */
public class TestRunnerImpl implements TestRunner {

    private final PersonService personService;
    private final TestService testService;

    public TestRunnerImpl(PersonService personService, TestService testService){
        this.personService = personService;
        this.testService = testService;
    }
    

    @Override
    public void runTest() {
        // запросить пользователя
        Person person = personService.getPerson();
        
        TestResult testResult = testService.processTest();

        System.out.println("-----------------");
        System.out.println("Испытуемый: " + person.getName() + " " + person.getLastName());
        System.out.println("Результат теста: " + testResult.getTestName());
        System.out.println("Верных ответов: " + testResult.getValue() + "%");
    }
}
