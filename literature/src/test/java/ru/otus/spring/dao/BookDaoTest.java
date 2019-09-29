package ru.otus.spring.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.domain.Book;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Проверка БД: книги")
@ExtendWith(SpringExtension.class)
@JdbcTest
@Import(BookDaoImpl.class)
public class BookDaoTest {
    
    @Autowired
    private BookDaoImpl bookDao;

    private static final String DB_TITLE_R = "Руслан и Людмила";
    private static final String DB_TITLE_1 = "1984";
    private static final String DB_TITLE_D = "Ромео и Джульетта";

    private static final String DB_AUTHOR_P = "Пушкин А.С.";
    private static final String DB_GENRE_S = "Сказка";
    private static final String DB_GENRE_D = "Драмма";

    @Test    
    @DisplayName("Получение всех книг")
    public void getAllTest(){
        List<Book> allBooks = bookDao.getAll();
        Assertions.assertEquals(3, allBooks.size(), "Должно быть 3 записи");
        List<String> authorBriefs = allBooks.stream().map(Book::getTitle).collect(Collectors.toList());
        assertThat(authorBriefs).contains(DB_TITLE_R, DB_TITLE_1, DB_TITLE_D);
    }
    
    @Test    
    @DisplayName("Поиск книг по параметрам - Название")
    public void getByParamTitleTest(){
        // простой поиск 
        String findTitle = DB_TITLE_R;
        List<Book> authorByName = bookDao.getByParam(findTitle, null, null);
        Assertions.assertEquals(1, authorByName.size(), "Должна быть 1 запись");
        List<String> foundGenreNames = authorByName.stream().map(Book::getTitle).collect(Collectors.toList());
        assertThat(foundGenreNames).contains(DB_TITLE_R);

        // поиск без %
        findTitle = "Руслан";
        authorByName = bookDao.getByParam(findTitle, null, null);
        Assertions.assertTrue(authorByName.isEmpty(), "Не должена найтись книга");

        // поиск с %
        findTitle = "Руслан%";
        authorByName = bookDao.getByParam(findTitle, null, null);
        Assertions.assertEquals(1, authorByName.size(), "Должна быть 1 запись");
        foundGenreNames = authorByName.stream().map(Book::getTitle).collect(Collectors.toList());
        assertThat(foundGenreNames).contains(DB_TITLE_R);

        // поиск с %
        findTitle = "%л%";
        authorByName = bookDao.getByParam(findTitle, null, null);
        Assertions.assertEquals(2, authorByName.size(), "Должно быть 2 записи");
        foundGenreNames = authorByName.stream().map(Book::getTitle).collect(Collectors.toList());
        assertThat(foundGenreNames).contains(DB_TITLE_R, DB_TITLE_D);
    }
    
    @Test    
    @DisplayName("Поиск книг по параметрам - Автор")
    public void getByParamAuthorTest(){
        // простой поиск 
        String findAuthor = DB_AUTHOR_P;
        List<Book> authorByName = bookDao.getByParam(null, findAuthor, null);
        Assertions.assertEquals(1, authorByName.size(), "Должна быть 1 запись");
        List<String> foundGenreNames = authorByName.stream().map(Book::getTitle).collect(Collectors.toList());
        assertThat(foundGenreNames).contains(DB_TITLE_R);

        // поиск без %
        findAuthor = "Пушкин";
        authorByName = bookDao.getByParam(null, findAuthor, null);
        Assertions.assertTrue(authorByName.isEmpty(), "Не должена найтись книга");

        // поиск с %
        findAuthor = "Пушкин%";
        authorByName = bookDao.getByParam(null, findAuthor, null);
        Assertions.assertEquals(1, authorByName.size(), "Должна быть 1 запись");
        foundGenreNames = authorByName.stream().map(Book::getTitle).collect(Collectors.toList());
        assertThat(foundGenreNames).contains(DB_TITLE_R);

        // поиск с %
        findAuthor = "%.%";
        authorByName = bookDao.getByParam(null, findAuthor, null);
        Assertions.assertEquals(3, authorByName.size(), "Должно быть 3 записи");
        foundGenreNames = authorByName.stream().map(Book::getTitle).collect(Collectors.toList());
        assertThat(foundGenreNames).contains(DB_TITLE_R, DB_TITLE_1, DB_TITLE_D);
    }
    @Test    
    @DisplayName("Поиск книг по параметрам - Жанр")
    public void getByParamGenreTest(){
        // простой поиск 
        String findGenre = DB_GENRE_S;
        List<Book> authorByName = bookDao.getByParam(null, null, findGenre);
        Assertions.assertEquals(1, authorByName.size(), "Должна быть 1 запись");
        List<String> foundGenreNames = authorByName.stream().map(Book::getTitle).collect(Collectors.toList());
        assertThat(foundGenreNames).contains(DB_TITLE_R);

        // поиск без %
        findGenre = "Сказ";
        authorByName = bookDao.getByParam(null, null, findGenre);
        Assertions.assertTrue(authorByName.isEmpty(), "Не должена найтись книга");

        // поиск с %
        findGenre = "Сказ%";
        authorByName = bookDao.getByParam(null, null, findGenre);
        Assertions.assertEquals(1, authorByName.size(), "Должна быть 1 запись");
        foundGenreNames = authorByName.stream().map(Book::getTitle).collect(Collectors.toList());
        assertThat(foundGenreNames).contains(DB_TITLE_R);

        // поиск с %
        findGenre = "%а%";
        authorByName = bookDao.getByParam(null, null, findGenre);
        Assertions.assertEquals(3, authorByName.size(), "Должно быть 3 записи");
        foundGenreNames = authorByName.stream().map(Book::getTitle).collect(Collectors.toList());
        assertThat(foundGenreNames).contains(DB_TITLE_R, DB_TITLE_1, DB_TITLE_D);
    }
    
    @Test    
    @DisplayName("Поиск книг по параметрам - Все параметры")
    public void getByParamComplexTest(){
        // простой поиск 
        String findTitle = DB_TITLE_R;
        String findAuthor = DB_AUTHOR_P;
        String findGenre = DB_GENRE_S;
        List<Book> authorByName = bookDao.getByParam(findTitle, findAuthor, findGenre);
        Assertions.assertEquals(1, authorByName.size(), "Должна быть 1 запись");
        List<String> foundGenreNames = authorByName.stream().map(Book::getTitle).collect(Collectors.toList());
        assertThat(foundGenreNames).contains(DB_TITLE_R);

        // поиск без %
        findTitle = "Руслан";
        findAuthor = "Пуш";
        findGenre = "Сказ";
        authorByName = bookDao.getByParam(findTitle, findAuthor, findGenre);
        Assertions.assertTrue(authorByName.isEmpty(), "Не должена найтись книга");

        // поиск с %
        findTitle = "Руслан%";
        findAuthor = "Пуш%";
        findGenre = "Сказ%";
        authorByName = bookDao.getByParam(findTitle, findAuthor, findGenre);
        Assertions.assertEquals(1, authorByName.size(), "Должна быть 1 запись");
        foundGenreNames = authorByName.stream().map(Book::getTitle).collect(Collectors.toList());
        assertThat(foundGenreNames).contains(DB_TITLE_R);

        // поиск без параметров
        authorByName = bookDao.getByParam(null, null, null);
        Assertions.assertEquals(3, authorByName.size(), "Должно быть 3 записи");
        foundGenreNames = authorByName.stream().map(Book::getTitle).collect(Collectors.toList());
        assertThat(foundGenreNames).contains(DB_TITLE_R, DB_TITLE_1, DB_TITLE_D);
    }
    
    @Test    
    @DisplayName("Поиск книг по id")
    public void getByIdTest(){
        List<Book> books = bookDao.getByParam(DB_TITLE_R, null, null);
        Book expectedBook = books.iterator().next();
        Book foundBook = bookDao.getById(expectedBook.getId());
        Assertions.assertEquals(expectedBook.getTitle(), foundBook.getTitle(), "Сокращение должно совпадать");
    }
    
    @Test    
    @DisplayName("Добавление книг")
    public void insertBookTest(){
        Book insertedBook = new Book(null, "Новая книга", 1L);
        // вставка
        Long bookId = bookDao.insertBook(insertedBook);
        // проверка
        Book foundBook = bookDao.getById(bookId);
        Assertions.assertEquals(insertedBook, foundBook, "Должны совпадать");
    }
    
    @Test    
    @DisplayName("Изменение книг")
    public void updateBookTest(){
        List<Book> books = bookDao.getByParam(DB_TITLE_R, null, null);
        Book expectedBook = books.iterator().next();

        // изменение
        expectedBook.setTitle("Другая книга");
        bookDao.updateBook(expectedBook);
        
        // проверка
        Book foundBook = bookDao.getById(expectedBook.getId());
        assertThat(foundBook).hasFieldOrPropertyWithValue("title","Другая книга");
    }
    
    @Test    
    @DisplayName("Удаление книг")
    public void deleteBookTest(){
        List<Book> books = bookDao.getByParam(DB_TITLE_R, null, null);
        Long bookId = books.iterator().next().getId();
        
        // удаление
        bookDao.deleteBook(bookId);

        // проверка
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> bookDao.getById(bookId));
    }
    
}
