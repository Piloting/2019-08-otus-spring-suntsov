package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookInfo;
import ru.otus.spring.domain.Genre;

import java.time.LocalDate;
import java.util.List;

@Service
public interface BookService {

    List<Book> getBooks(String title, String authorBrief, String genreName);
    List<BookInfo> getBooksInfo(String title, String authorBrief, String genreName);

    Book getById(Long id);
    Book getByName(String name);
    
    void addBook(String name, LocalDate releaseDate, String author);
    void addBookGenre(Book book, Genre genre);
    void deleteBookGenre(Book book, Genre genre);

    Long insertBook(Book book, List<Genre> genres);

    void updateBook(Book book);
    void deleteBook(Long bookId);
}
