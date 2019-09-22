package ru.otus.spring;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.spring.domain.TestResult;
import ru.otus.spring.service.ChannelService;
import ru.otus.spring.service.TestService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessTest {

    @Autowired
    private ApplicationContext applicationContext;

    @MockBean
    ChannelService channelService;
    
    @Test
    public void contextLoads() {
        Mockito.when(channelService.listen()).then(invocationOnMock -> {
            System.out.println("1");
            return "1";
        });
        
        Mockito.doAnswer(invocationOnMock -> {
            Object arg0 = invocationOnMock.getArgument(0);
            System.out.println(arg0);
            return null;
        }).when(channelService).say(Mockito.any());

        TestService testService = applicationContext.getBean(TestService.class);
        TestResult testResult = testService.processTest();

        Assert.assertEquals("Result 60%", 60, testResult.getValue());
    }
}
