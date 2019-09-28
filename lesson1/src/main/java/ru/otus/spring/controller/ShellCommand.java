package ru.otus.spring.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.spring.common.LocalizationService;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.TestResult;
import ru.otus.spring.service.PersonService;
import ru.otus.spring.service.TestService;

@ShellComponent
public class ShellCommand {
    
    private final PersonService personService;
    private final TestService testService;
    private final LocalizationService localizationService;
    private final Integer successPercent;
    
    private TestResult testResult;
    private Person person;

    public ShellCommand(PersonService personService,
                          TestService testService,
                          @Value("${successPercent}") String successPercentConfig,
                          LocalizationService localizationService){
        this.personService = personService;
        this.testService = testService;
        this.successPercent = Integer.parseInt(successPercentConfig);
        this.localizationService = localizationService;
    }
    
    @ShellMethod(value = "Login command", key = {"l", "login"})
    public void login() {
        this.person = personService.getPerson();
    }
    
    @ShellMethod(value = "Start test", key = {"t", "test"})
    @ShellMethodAvailability(value = "isLogin")
    public void startTest() {
        this.testResult = testService.processTest();
    }
    
    @ShellMethod(value = "Get result test", key = {"r", "result"})
    @ShellMethodAvailability(value = "isTest")
    public String getResultTest() {
        StringBuilder sb = new StringBuilder();
        sb.append(localizationService.getMessage("student", person.getFullName())).append("\n");
        sb.append(localizationService.getMessage("test_no", testResult.getTestName())).append("\n");
        sb.append(localizationService.getMessage("correct_answers",  testResult.getValue())).append("\n");
        sb.append(localizationService.getMessage(successPercent > testResult.getValue() ? "fail_exam" : "pass_exam"));
        return sb.toString();
    }
    
    private Availability isTest() {
        return testResult == null? Availability.unavailable("Необходимо пройдите тест"): Availability.available();
    }
    private Availability isLogin() {
        return person == null? Availability.unavailable("Необходимо залогиниться"): Availability.available();
    }

}
