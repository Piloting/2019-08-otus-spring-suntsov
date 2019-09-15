package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.common.LocalMessage;
import ru.otus.spring.domain.Person;

@Service
public class PersonServiceImpl implements PersonService {

    private final ChannelService channel;
    private final LocalMessage localMessage;
    
    public PersonServiceImpl(ChannelService channel, LocalMessage localMessage){
        this.channel = channel;
        this.localMessage = localMessage;
    }
    
    @Override
    public Person getPerson() {
        channel.say(localMessage.getMessage("say_name"));
        String answer = channel.listen();
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
}
