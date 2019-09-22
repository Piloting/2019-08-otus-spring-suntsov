package ru.otus.spring;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.domain.TestResult;
import ru.otus.spring.service.ChannelService;
import ru.otus.spring.service.TestService;

@DisplayName("Проверка прохождения теста")
@SpringBootTest
public class TestServiceTest {

    @Autowired
    TestService testService;

    @MockBean
    ChannelService channelService;
    
    @DisplayName(" пройдена успешно")
    @Test
    public void processTest() {
        Mockito.when(channelService.listen()).then(invocationOnMock -> {
            System.out.println("1");
            return "1";
        });
        
        Mockito.doAnswer(invocationOnMock -> {
            Object arg0 = invocationOnMock.getArgument(0);
            System.out.println(arg0);
            return null;
        }).when(channelService).say(Mockito.any());

        TestResult testResult = testService.processTest();

        Assertions.assertEquals(60, testResult.getValue(), "Result 60%");
    }
}
