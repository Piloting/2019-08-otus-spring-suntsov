package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookInfo;
import ru.otus.spring.domain.Genre;

import java.util.List;

@Service
public interface BookService {

    List<Book> getBooksByParam(String title, String authorBrief, String genreName);
    List<BookInfo> getBookFullInfoByParam(String title, String authorBrief, String genreName);

    Long insertBook(Book book, List<Genre> genres);

    void updateBook(Book book);
    void deleteBook(Long bookId);
}
