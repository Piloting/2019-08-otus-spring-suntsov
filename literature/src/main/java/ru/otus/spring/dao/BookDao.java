package ru.otus.spring.dao;

import ru.otus.spring.domain.Book;

import java.util.List;

public interface BookDao {
    List<Book> getAll();
    List<Book> getByParam(String title, String authorBrief, String genreName);
    
    Book getById(Long id);
    Book getByName(String name);

    Long insertBook(Book book);
    void updateBook(Book book);
    void deleteBook(Long bookId);
}
