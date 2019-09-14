package ru.otus.spring.service;

import java.util.Scanner;

public class ScannerChannelServiceImpl implements ChannelService {

    private final Scanner scanner;
    
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
