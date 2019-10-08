package ru.otus.spring.dao;

import ru.otus.spring.domain.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface GenreDao {
    List<Genre> findGenresByName(String name);
    List<Genre> findAllGenres();

    Map<Long, List<Genre>> getGenresByBookIds(Collection<Long> bookIds);
    void insertBookGenres(Map<Long, List<Genre>> bookGenres);
    void deleteBookGenreByBookId(Long bookId);
}
