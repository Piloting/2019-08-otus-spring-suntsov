package ru.otus.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Scanner;

@Service
public class ScannerChannelServiceImpl implements ChannelService {

    private final Scanner scanner;
    
    @Autowired
    public ScannerChannelServiceImpl(){
        this.scanner = new Scanner(System.in);
        this.scanner.useDelimiter("\n");
    }
    
    public ScannerChannelServiceImpl(Scanner scanner){
        this.scanner = scanner;
        this.scanner.useDelimiter("\n");
    }
    
    @Override
    public void say(String string) {
        System.out.println(string);
    }

    @Override
    public String listen() {
        return scanner.next();
    }
}
