package ru.otus.spring.service;

import ru.otus.spring.domain.Author;

import java.util.Collection;
import java.util.List;

public interface AuthorService {
    List<Author> findAuthors(String brief);
    List<Author> getAuthorByIds(Collection<Long> ids);
    Author getAuthor(Long id, String brief);
}
