package ru.otus.spring.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.dao.dto.Book;
import ru.otus.spring.domain.BookInfo;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Проверка БД: книги")
@ExtendWith(SpringExtension.class)
@JdbcTest
@Import({BookDaoImpl.class,  GenreDaoImpl.class, AuthorDaoImpl.class})
public class BookDaoTest {
    
    @Autowired
    private BookDaoImpl bookDao;

    private static final String DB_TITLE_R = "Руслан и Людмила";
    private static final String DB_TITLE_1 = "1984";
    private static final String DB_TITLE_D = "Ромео и Джульетта";

    private static final String DB_AUTHOR_P = "Пушкин А.С.";
    private static final String DB_GENRE_S = "Сказка";

    @Test    
    @DisplayName("Получение всех книг")
    public void getAllTest(){
        List<BookInfo> allBooks = bookDao.getAll();
        Assertions.assertEquals(3, allBooks.size(), "Должно быть 3 записи");
        List<String> titles = allBooks.stream().map(BookInfo::getTitle).collect(Collectors.toList());
        assertThat(titles).contains(DB_TITLE_R, DB_TITLE_1, DB_TITLE_D);
    }
    
    @Test    
    @DisplayName("Поиск книг по параметрам - Название")
    public void getByParamTitleTest(){
        // простой поиск 
        String findTitle = DB_TITLE_R;
        List<BookInfo> bookInfoByTitleList = bookDao.getByParam(findTitle, null, null);
        Assertions.assertEquals(1, bookInfoByTitleList.size(), "Должна быть 1 запись");
        List<String> foundTitles = bookInfoByTitleList.stream().map(BookInfo::getTitle).collect(Collectors.toList());
        assertThat(foundTitles).contains(DB_TITLE_R);

        // поиск без %
        findTitle = "Руслан";
        bookInfoByTitleList = bookDao.getByParam(findTitle, null, null);
        Assertions.assertTrue(bookInfoByTitleList.isEmpty(), "Не должена найтись книга");

        // поиск с %
        findTitle = "Руслан%";
        bookInfoByTitleList = bookDao.getByParam(findTitle, null, null);
        Assertions.assertEquals(1, bookInfoByTitleList.size(), "Должна быть 1 запись");
        foundTitles = bookInfoByTitleList.stream().map(BookInfo::getTitle).collect(Collectors.toList());
        assertThat(foundTitles).contains(DB_TITLE_R);

        // поиск с %
        findTitle = "%л%";
        bookInfoByTitleList = bookDao.getByParam(findTitle, null, null);
        Assertions.assertEquals(2, bookInfoByTitleList.size(), "Должно быть 2 записи");
        foundTitles = bookInfoByTitleList.stream().map(BookInfo::getTitle).collect(Collectors.toList());
        assertThat(foundTitles).contains(DB_TITLE_R, DB_TITLE_D);
    }
    
    @Test    
    @DisplayName("Поиск книг по параметрам - Автор")
    public void getByParamAuthorTest(){
        // простой поиск 
        String findAuthor = DB_AUTHOR_P;
        List<BookInfo> bookInfoByAuthorList = bookDao.getByParam(null, findAuthor, null);
        Assertions.assertEquals(1, bookInfoByAuthorList.size(), "Должна быть 1 запись");
        List<String> foundTitles = bookInfoByAuthorList.stream().map(BookInfo::getTitle).collect(Collectors.toList());
        assertThat(foundTitles).contains(DB_TITLE_R);

        // поиск без %
        findAuthor = "Пушкин";
        bookInfoByAuthorList = bookDao.getByParam(null, findAuthor, null);
        Assertions.assertTrue(bookInfoByAuthorList.isEmpty(), "Не должена найтись книга");

        // поиск с %
        findAuthor = "Пушкин%";
        bookInfoByAuthorList = bookDao.getByParam(null, findAuthor, null);
        Assertions.assertEquals(1, bookInfoByAuthorList.size(), "Должна быть 1 запись");
        foundTitles = bookInfoByAuthorList.stream().map(BookInfo::getTitle).collect(Collectors.toList());
        assertThat(foundTitles).contains(DB_TITLE_R);

        // поиск с %
        findAuthor = "%.%";
        bookInfoByAuthorList = bookDao.getByParam(null, findAuthor, null);
        Assertions.assertEquals(3, bookInfoByAuthorList.size(), "Должно быть 3 записи");
        foundTitles = bookInfoByAuthorList.stream().map(BookInfo::getTitle).collect(Collectors.toList());
        assertThat(foundTitles).contains(DB_TITLE_R, DB_TITLE_1, DB_TITLE_D);
    }
    @Test    
    @DisplayName("Поиск книг по параметрам - Жанр")
    public void getByParamGenreTest(){
        // простой поиск 
        String findGenre = DB_GENRE_S;
        List<BookInfo> bookInfoByGenreList = bookDao.getByParam(null, null, findGenre);
        Assertions.assertEquals(1, bookInfoByGenreList.size(), "Должна быть 1 запись");
        List<String> foundTitles = bookInfoByGenreList.stream().map(BookInfo::getTitle).collect(Collectors.toList());
        assertThat(foundTitles).contains(DB_TITLE_R);

        // поиск без %
        findGenre = "Сказ";
        bookInfoByGenreList = bookDao.getByParam(null, null, findGenre);
        Assertions.assertTrue(bookInfoByGenreList.isEmpty(), "Не должена найтись книга");

        // поиск с %
        findGenre = "Сказ%";
        bookInfoByGenreList = bookDao.getByParam(null, null, findGenre);
        Assertions.assertEquals(1, bookInfoByGenreList.size(), "Должна быть 1 запись");
        foundTitles = bookInfoByGenreList.stream().map(BookInfo::getTitle).collect(Collectors.toList());
        assertThat(foundTitles).contains(DB_TITLE_R);

        // поиск с %
        findGenre = "%а%";
        bookInfoByGenreList = bookDao.getByParam(null, null, findGenre);
        Assertions.assertEquals(3, bookInfoByGenreList.size(), "Должно быть 3 записи");
        foundTitles = bookInfoByGenreList.stream().map(BookInfo::getTitle).collect(Collectors.toList());
        assertThat(foundTitles).contains(DB_TITLE_R, DB_TITLE_1, DB_TITLE_D);
    }
    
    @Test    
    @DisplayName("Поиск книг по параметрам - Все параметры")
    public void getByParamComplexTest(){
        // простой поиск 
        String findTitle = DB_TITLE_R;
        String findAuthor = DB_AUTHOR_P;
        String findGenre = DB_GENRE_S;
        List<BookInfo> foundBookInfoList = bookDao.getByParam(findTitle, findAuthor, findGenre);
        Assertions.assertEquals(1, foundBookInfoList.size(), "Должна быть 1 запись");
        List<String> foundTitles = foundBookInfoList.stream().map(BookInfo::getTitle).collect(Collectors.toList());
        assertThat(foundTitles).contains(DB_TITLE_R);

        // поиск без %
        findTitle = "Руслан";
        findAuthor = "Пуш";
        findGenre = "Сказ";
        foundBookInfoList = bookDao.getByParam(findTitle, findAuthor, findGenre);
        Assertions.assertTrue(foundBookInfoList.isEmpty(), "Не должена найтись книга");

        // поиск с %
        findTitle = "Руслан%";
        findAuthor = "Пуш%";
        findGenre = "Сказ%";
        foundBookInfoList = bookDao.getByParam(findTitle, findAuthor, findGenre);
        Assertions.assertEquals(1, foundBookInfoList.size(), "Должна быть 1 запись");
        foundTitles = foundBookInfoList.stream().map(BookInfo::getTitle).collect(Collectors.toList());
        assertThat(foundTitles).contains(DB_TITLE_R);

        // поиск без параметров
        foundBookInfoList = bookDao.getByParam(null, null, null);
        Assertions.assertEquals(3, foundBookInfoList.size(), "Должно быть 3 записи");
        foundTitles = foundBookInfoList.stream().map(BookInfo::getTitle).collect(Collectors.toList());
        assertThat(foundTitles).contains(DB_TITLE_R, DB_TITLE_1, DB_TITLE_D);
    }
    
    @Test    
    @DisplayName("Поиск книг по id")
    public void getByIdTest(){
        List<BookInfo> bookInfoList = bookDao.getByParam(DB_TITLE_R, null, null);
        BookInfo expectedBookInfo = bookInfoList.iterator().next();
        Book foundBook = bookDao.getById(expectedBookInfo.getId());
        Assertions.assertEquals(expectedBookInfo.getTitle(), foundBook.getTitle(), "Сокращение должно совпадать");
    }
    
    @Test    
    @DisplayName("Добавление книг")
    public void insertBookTest(){
        BookInfo insertedBookInfo = new BookInfo();
        insertedBookInfo.setTitle("Новая книга");
        // вставка
        Long bookId = bookDao.insertBook(insertedBookInfo);
        // проверка
        Book foundBook = bookDao.getById(bookId);
        Assertions.assertEquals(insertedBookInfo.getTitle(), foundBook.getTitle(), "Должны совпадать");
    }
    
    @Test    
    @DisplayName("Изменение книг")
    public void updateBookTest(){
        List<BookInfo> bookInfoList = bookDao.getByParam(DB_TITLE_R, null, null);
        BookInfo expectedBookInfo = bookInfoList.iterator().next();

        // изменение
        expectedBookInfo.setTitle("Другая книга");
        bookDao.updateBook(expectedBookInfo);
        
        // проверка
        Book foundBook = bookDao.getById(expectedBookInfo.getId());
        assertThat(foundBook).hasFieldOrPropertyWithValue("title","Другая книга");
    }
    
    @Test    
    @DisplayName("Удаление книг")
    public void deleteBookTest(){
        List<BookInfo> bookInfoList = bookDao.getByParam(DB_TITLE_R, null, null);
        Long bookId = bookInfoList.iterator().next().getId();
        // проверка
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> bookDao.deleteBook(bookId));
    }
    
}
