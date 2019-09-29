package ru.otus.spring.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookInfo;
import ru.otus.spring.domain.Genre;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Проверка сервисов: книги")
@SpringBootTest
public class BookServiceTest {
    
    @MockBean
    private BookDao bookDao;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;
    @Autowired
    private BookService bookService;
    
    @Test
    @DisplayName("Поиск книг")
    public void getBooksByParamTest(){
        // без параметров - проверяем, что вызвался метод findAllGenres
        bookService.getBooksByParam(null, null, null);
        verify(bookDao, times(1)).getAll();

        // с параметром - проверяем, что вызвался метод findGenresByName с верным параметром
        bookService.getBooksByParam("value", null, null);
        verify(bookDao, times(1)).getByParam(eq("value"), isNull(), isNull());
    }
    
    @Test
    @DisplayName("Поиск полной информации о книгах")
    public void getBooksInfoByParamTest(){
        Book book = new Book(1L, "Book", 1L);
        Mockito.when(bookDao.getByParam("title", "author", "genre")).thenReturn(Collections.singletonList(book));

        List<BookInfo> bookFullInfoByParam = bookService.getBookFullInfoByParam("title", "author", "genre");
        Assertions.assertEquals(1, bookFullInfoByParam.size(), "Должна быть 1 запись");

        Assertions.assertEquals(book, bookFullInfoByParam.iterator().next().getBook(), "Должны совпадать");
        
        verify(bookDao, times(1)).getByParam(eq("title"), eq("author"), eq("genre"));
        verify(authorService, times(1)).getAuthorsByIds(any());
        verify(genreService, times(1)).getGenresByBookIds(any());
    }

    @Test
    @DisplayName("Добавление книги")
    public void insertBookTest(){
        Book book = new Book(null, "Book", 1L);
        List<Genre> genres = Collections.singletonList(new Genre(1L, "genre1"));
        bookService.insertBook(book, genres);

        verify(bookDao, times(1)).insertBook(eq(book));
        verify(genreService, times(1)).bindGenres(any(), eq(genres));
    }

    @Test
    @DisplayName("Удаление книги")
    public void deleteBookTest(){
        Long bookId = 10L;
        bookService.deleteBook(bookId);
        verify(genreService, times(1)).clearGenresBook(eq(bookId));
        verify(bookDao, times(1)).deleteBook(eq(bookId));
    }
}
