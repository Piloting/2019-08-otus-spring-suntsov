package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.dao.BookDao;
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
    @DisplayName("Добавление книги")
    public void insertBookTest(){
        BookInfo bookInfo = new BookInfo();
        bookInfo.setTitle("Book");
        List<Genre> genres = Collections.singletonList(new Genre(1L, "genre1"));
        bookInfo.setGenres(genres);
        bookService.insertBook(bookInfo);

        verify(bookDao, times(1)).insertBook(eq(bookInfo));
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
