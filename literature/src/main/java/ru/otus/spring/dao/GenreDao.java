package ru.otus.spring.dao;

import ru.otus.spring.domain.BookGenre;
import ru.otus.spring.domain.Genre;

import java.util.Collection;
import java.util.List;

public interface GenreDao {
    List<Genre> findGenresByName(String name);
    List<Genre> findAllGenres();
    
    List<BookGenre> getGenresByBookIds(Collection<Long> bookIds);
    void insertBookGenres(List<BookGenre> bookGenres);
    void deleteBookGenreByBookId(Long bookId);
}
