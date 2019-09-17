package ru.otus.spring.domain;

import lombok.Data;

@Data
public class Person {
    private String name;
    private String lastName;
    
    public String getName(){
        return name != null ? name : "";
    }
    public String getLastName(){
        return lastName != null ? lastName : "";
    }
    
    public String getFullName(){
        return getName() + " " + getLastName();
    }
}
