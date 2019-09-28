package ru.otus.spring.dao;

import ru.otus.spring.domain.Author;

import java.util.Collection;
import java.util.List;

public interface AuthorDao {
    List<Author> findAuthorsByBrief(String brief);
    List<Author> findAllAuthors();
    List<Author> getAuthorByIds(Collection<Long> ids);
}
