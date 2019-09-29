package ru.otus.spring.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Genre;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Проверка сервисов: жанры")
@SpringBootTest
public class GenreServiceTest {
    
    @MockBean
    private GenreDao genreDao;

    @Autowired
    private GenreService genreService;
    
    @Test
    @DisplayName("Поиск жанров")
    public void findGenresTest(){
        // без параметров - проверяем, что вызвался метод findAllGenres
        genreService.findGenresByName("");
        verify(genreDao, times(1)).findAllGenres();

        // с параметром - проверяем, что вызвался метод findGenresByName с верным параметром
        genreService.findGenresByName("value");
        verify(genreDao, times(1)).findGenresByName(eq("value"));
    }
    
    @Test
    @DisplayName("Получить жанры")
    public void getGenresTest(){
        // успешный поиск по name
        Genre genre1 = new Genre(1L, "genre1");
        Mockito.when(genreDao.findGenresByName("genre1")).thenReturn(Collections.singletonList(genre1));
        List<Genre> foundGenres = genreService.getGenresByNames(Collections.singletonList("genre1"));
        verify(genreDao, times(1)).findGenresByName(eq("genre1"));
        assertThat(foundGenres).containsOnly(genre1);

        // что-то не найдно
        Assertions.assertThrows(RuntimeException.class, () -> genreService.getGenresByNames(Collections.singletonList("notFoundGenre")));
    }

    @Test
    @DisplayName("Привязка жанров")
    public void bindGenresTest(){
        genreService.bindGenres(1L, new ArrayList<>(0));
        verify(genreDao, times(0)).insertBookGenres(any());
        
        genreService.bindGenres(1L, Arrays.asList(new Genre(), new Genre()));
        verify(genreDao, times(1)).insertBookGenres(any());
    }
}
