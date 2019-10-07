package ru.otus.spring.dao;

import ru.otus.spring.domain.BookInfo;

import java.util.List;

public interface BookDao {
    List<BookInfo> getAll();
    List<BookInfo> getByParam(String title, String authorBrief, String genreName);
    
    BookInfo getById(Long id);

    Long insertBook(BookInfo bookInfo);
    void updateBook(BookInfo bookInfo);
    void deleteBook(Long bookId);
}
