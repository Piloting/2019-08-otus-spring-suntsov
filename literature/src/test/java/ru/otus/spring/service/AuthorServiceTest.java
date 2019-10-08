package ru.otus.spring.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.domain.Author;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Проверка сервисов: автор")
@SpringBootTest
public class AuthorServiceTest {

    @MockBean
    private AuthorDao authorDao;
    
    @Autowired
    private AuthorService authorService;

    @Test
    @DisplayName("Поиск авторов по сокращению")
    public void findAuthorsByBrief(){
        // без параметров - проверяем, что вызвался метод findAllAuthors
        authorService.findAuthorsByBrief("");
        verify(authorDao, times(1)).findAllAuthors();

        // с параметром - проверяем, что вызвался метод findAuthorsByBrief с верным параметром
        authorService.findAuthorsByBrief("value");
        verify(authorDao, times(1)).findAuthorsByBrief(eq("value"));
    }

    @Test
    @DisplayName("Получить конкретного автора")
    public void getAuthor(){
        // при вызове без конкретных значений
        Assertions.assertThrows(RuntimeException.class, () -> authorService.getAuthor(null, null));
        
        // если передаем ID - вызывается поиск по ID
        Mockito.when(authorDao.getAuthorByIds(any())).thenReturn(Arrays.asList(new Author()));
        authorService.getAuthor(1L, "");
        verify(authorDao, times(1)).getAuthorByIds(Collections.singleton(1L));

        // если передаем brief - вызывается поиск по brief
        Mockito.when(authorDao.findAuthorsByBrief("brief")).thenReturn(Collections.singletonList(new Author()));
        authorService.getAuthor(null, "brief");
        verify(authorDao, times(1)).findAuthorsByBrief(eq("brief"));
        
        // если надется более 1
        Mockito.when(authorDao.findAuthorsByBrief("%.%")).thenReturn(Arrays.asList(new Author(), new Author()));
        Assertions.assertThrows(RuntimeException.class, () -> authorService.getAuthor(null, "%.%"));
    }
}
