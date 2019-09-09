package ru.otus.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.spring.domain.TestResult;
import ru.otus.spring.service.TestService;

public class Main {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        TestService testService = context.getBean(TestService.class);
        TestResult result = testService.processTest();
        System.out.println("Результат теста: " + result.getTestName());
        System.out.println("Верных ответов: " + result.getValue() + "%");
    }
}
