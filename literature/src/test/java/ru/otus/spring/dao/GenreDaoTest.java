package ru.otus.spring.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.domain.BookInfo;
import ru.otus.spring.domain.Genre;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Проверка БД: жанры")
@ExtendWith(SpringExtension.class)
@JdbcTest
@Import({GenreDaoImpl.class, BookDaoImpl.class, AuthorDaoImpl.class})
public class GenreDaoTest {
    
    @Autowired
    private GenreDaoImpl genreDao;
    @Autowired
    private BookDaoImpl bookDao;
    
    private static final String DB_NAME_S = "Сказка";
    private static final String DB_NAME_F = "Фантастика";
    private static final String DB_NAME_D = "Драмма";

    @Test
    @DisplayName("Поиск по названию")
    public void findGenresByNameTest(){
        // простой поиск 
        String findName = DB_NAME_S;
        List<Genre> genresByName = genreDao.findGenresByName(findName);
        Assertions.assertEquals(1, genresByName.size(), "Должна быть 1 запись");
        List<String> foundGenreNames = genresByName.stream().map(Genre::getName).collect(Collectors.toList());
        assertThat(foundGenreNames).contains(DB_NAME_S);

        // поиск без %
        findName = "Сказ";
        genresByName = genreDao.findGenresByName(findName);
        Assertions.assertTrue(genresByName.isEmpty(), "Не должен найтись жанр");

        // поиск с %
        findName = "Сказ%";
        genresByName = genreDao.findGenresByName(findName);
        Assertions.assertEquals(1, genresByName.size(), "Должна быть 1 запись");
        foundGenreNames = genresByName.stream().map(Genre::getName).collect(Collectors.toList());
        assertThat(foundGenreNames).contains(DB_NAME_S);

        // поиск с %
        findName = "%а%";
        genresByName = genreDao.findGenresByName(findName);
        Assertions.assertEquals(3, genresByName.size(), "Должно быть 3 записи");
        foundGenreNames = genresByName.stream().map(Genre::getName).collect(Collectors.toList());
        assertThat(foundGenreNames).contains(DB_NAME_S, DB_NAME_F, DB_NAME_D);
    }

    @Test
    @DisplayName("Поиск всех")
    public void findAllGenresTest(){
        List<Genre> allGenres = genreDao.findAllGenres();
        Assertions.assertEquals(3, allGenres.size(), "Должно быть 3 записи");
        List<String> genreNames = allGenres.stream().map(Genre::getName).collect(Collectors.toList());
        assertThat(genreNames).contains(DB_NAME_S, DB_NAME_F, DB_NAME_D);
    }

    @Test    
    @DisplayName("Поиск жанров конкретных книг")
    public void getGenresByBookIdsTest(){
        List<BookInfo> booksInfo = bookDao.getByParam("Руслан и Людмила", null, null);
        Long bookId = booksInfo.iterator().next().getId();

        Map<Long, List<Genre>> genresByBookIds = genreDao.getGenresByBookIds(Collections.singleton(bookId));
        
        // на выходе только искомая книга
        Set<Long> bookIds = genresByBookIds.keySet();
        assertThat(bookIds).containsOnly(bookId);
        
        // на выходе правильные жанры
        List<String> genres = genresByBookIds.values().stream().flatMap(Collection::stream).map(Genre::getName).collect(Collectors.toList());
        assertThat(genres).contains(DB_NAME_S, DB_NAME_D);
    }

    @Test
    @DisplayName("Добавление жанров для книги")
    public void insertBookGenresTest(){
        // данные
        Long bookId = 1L;
        List<Genre> genres = genreDao.findGenresByName(DB_NAME_S);
        // вставка
        genreDao.insertBookGenres(Collections.singletonMap(bookId, genres));
        // проверка
        Map<Long, List<Genre>> genresByBookIds = genreDao.getGenresByBookIds(Collections.singleton(bookId));
        List<String> foundGenres = genresByBookIds.values().stream().flatMap(Collection::stream).map(Genre::getName).collect(Collectors.toList());
        assertThat(foundGenres).contains(DB_NAME_S);
    }

    @Test
    @DisplayName("Удаление жанров для книги")
    public void deleteBookGenreByBookIdTest(){
        List<BookInfo> booksInfo = bookDao.getByParam("Руслан и Людмила", null, null);
        Long bookId = booksInfo.iterator().next().getId();
        
        // удаление
        genreDao.deleteBookGenreByBookId(bookId);
        // проверка
        Map<Long, List<Genre>> genresByBookIds = genreDao.getGenresByBookIds(Collections.singleton(bookId));
        Assertions.assertTrue(genresByBookIds.isEmpty(), "Не должно быть жанров");
    }
}
