package ru.otus.spring.domain;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class BookInfo {
    private Book book;
    private Author author;
    private List<Genre> genres;
    
    @Override
    public String toString(){
        return "Книга: " + book.getTitle() + 
               ", Автор: " + author.getBrief() + 
               ", Жанр: " + (genres != null ? genres.stream().map(Genre::getName).collect(Collectors.toSet()) : "<н/д>");
    }
}
