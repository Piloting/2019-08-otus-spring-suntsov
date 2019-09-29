package ru.otus.spring.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookGenre;
import ru.otus.spring.domain.Genre;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Проверка БД: жанры")
@ExtendWith(SpringExtension.class)
@JdbcTest
@Import({GenreDaoImpl.class, BookDaoImpl.class})
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

    @Test    @DisplayName("Поиск жанров конкретных книг")
    public void getGenresByBookIdsTest(){
        List<Book> books = bookDao.getByParam("Руслан и Людмила", null, null);
        Long bookId = books.iterator().next().getId();

        List<BookGenre> genresByBookIds = genreDao.getGenresByBookIds(Collections.singleton(bookId));
        
        // на выходе только искомая книга
        List<Long> bookIds = genresByBookIds.stream().map(BookGenre::getBookId).collect(Collectors.toList());
        assertThat(bookIds).containsOnly(bookId);
        
        // на выходе правильные жанры
        List<String> genres = genresByBookIds.stream().map(bookGenre -> bookGenre.getGenre().getName()).collect(Collectors.toList());
        assertThat(genres).contains(DB_NAME_S, DB_NAME_D);
    }

    @Test
    @DisplayName("Добавление жанров для книги")
    public void insertBookGenresTest(){
        // данные
        Long bookId = 10L;
        List<BookGenre> bookGenres = createBookGenreDto(bookId);
        // вставка
        genreDao.insertBookGenres(bookGenres);
        // проверка
        List<BookGenre> genresByBookIds = genreDao.getGenresByBookIds(Collections.singleton(bookId));
        List<String> genres = genresByBookIds.stream().map(bookGenre -> bookGenre.getGenre().getName()).collect(Collectors.toList());
        assertThat(genres).contains(DB_NAME_S);
    }

    private List<BookGenre> createBookGenreDto(Long bookId) {
        List<Genre> genresByName = genreDao.findGenresByName(DB_NAME_S);
        List<BookGenre> bookGenres = new ArrayList<>();
        for (Genre genre : genresByName) {
            bookGenres.add(new BookGenre(bookId, genre));
        }
        return bookGenres;
    }

    @Test
    @DisplayName("Удаление жанров для книги")
    public void deleteBookGenreByBookIdTest(){
        List<Book> books = bookDao.getByParam("Руслан и Людмила", null, null);
        Long bookId = books.iterator().next().getId();
        
        // удаление
        genreDao.deleteBookGenreByBookId(bookId);
        // проверка
        List<BookGenre> genresByBookIds = genreDao.getGenresByBookIds(Collections.singleton(bookId));
        Assertions.assertTrue(genresByBookIds.isEmpty(), "Не должно быть жанров");
    }
}
