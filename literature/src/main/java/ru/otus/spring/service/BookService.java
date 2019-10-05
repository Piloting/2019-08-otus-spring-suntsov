package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.BookInfo;

import java.util.List;

@Service
public interface BookService {

    List<BookInfo> getBooksByParam(String title, String authorBrief, String genreName);

    Long insertBook(BookInfo bookInfo);

    void updateBook(BookInfo bookInfo);
    void deleteBook(Long bookId);
}
