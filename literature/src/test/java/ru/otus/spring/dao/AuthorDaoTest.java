package ru.otus.spring.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.domain.Author;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Проверка БД: авторы")
@ExtendWith(SpringExtension.class)
@JdbcTest
@Import(AuthorDaoImpl.class)
public class AuthorDaoTest {

    @Autowired
    private AuthorDaoImpl authorDao;
    private static final String DB_BRIEF_P = "Пушкин А.С.";
    private static final String DB_BRIEF_O = "Оруэл Д.";
    private static final String DB_BRIEF_S = "Шекспир У.";

    @Test
    @DisplayName("Поиск по сокращению")
    public void findAuthorsByBriefTest(){
        // простой поиск 
        String findBrief = DB_BRIEF_P;
        List<Author> authorsByBrief = authorDao.findAuthorsByBrief(findBrief);
        Assertions.assertEquals(1, authorsByBrief.size(), "Должна быть 1 запись");
        List<String> authorBriefs = authorsByBrief.stream().map(Author::getBrief).collect(Collectors.toList());
        assertThat(authorBriefs).contains(DB_BRIEF_P);
        
        // поиск без %
        findBrief = "Пушкин";
        authorsByBrief = authorDao.findAuthorsByBrief(findBrief);
        Assertions.assertTrue(authorsByBrief.isEmpty(), "Не должен найтись автор");
        
        // поиск с %
        findBrief = "Пушкин%";
        authorsByBrief = authorDao.findAuthorsByBrief(findBrief);
        Assertions.assertEquals(1, authorsByBrief.size(), "Должна быть 1 запись");
        authorBriefs = authorsByBrief.stream().map(Author::getBrief).collect(Collectors.toList());
        assertThat(authorBriefs).contains(DB_BRIEF_P);
        
        // поиск с %
        findBrief = "%.%";
        authorsByBrief = authorDao.findAuthorsByBrief(findBrief);
        Assertions.assertEquals(3, authorsByBrief.size(), "Должно быть 3 записи");
        authorBriefs = authorsByBrief.stream().map(Author::getBrief).collect(Collectors.toList());
        assertThat(authorBriefs).contains(DB_BRIEF_P, DB_BRIEF_O, DB_BRIEF_S);
    }


    @Test
    @DisplayName("Поиск всех")
    public void findAllAuthorsTest(){
        List<Author> allAuthors = authorDao.findAllAuthors();
        Assertions.assertEquals(3, allAuthors.size(), "Должно быть 3 записи");
        List<String> authorBriefs = allAuthors.stream().map(Author::getBrief).collect(Collectors.toList());
        assertThat(authorBriefs).contains(DB_BRIEF_P, DB_BRIEF_O, DB_BRIEF_S);
    }

    @Test
    @DisplayName("Поиск по ID")
    public void getAuthorByIdsTest(){
        List<Author> authorsByBrief = authorDao.findAuthorsByBrief(DB_BRIEF_P);
        Author expectedAuthor = authorsByBrief.iterator().next();
        
        List<Author> authorByIds = authorDao.getAuthorByIds(Collections.singleton(expectedAuthor.getId()));
        Assertions.assertEquals(1, authorByIds.size(), "Должна быть 1 запись");
        Author foundAuthor = authorByIds.iterator().next();
        Assertions.assertEquals(expectedAuthor.getBrief(), foundAuthor.getBrief(), "Сокращение должно совпадать");
    }
}
