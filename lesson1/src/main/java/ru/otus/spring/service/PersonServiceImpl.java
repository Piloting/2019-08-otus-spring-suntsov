package ru.otus.spring.service;

import ru.otus.spring.domain.Person;

public class PersonServiceImpl implements PersonService {

    private final ChannelService channel;
    
    public PersonServiceImpl(ChannelService channel){
        this.channel = channel;
    }
    
    @Override
    public Person getPerson() {
        channel.say("Введите имя и фамилию: ");
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
