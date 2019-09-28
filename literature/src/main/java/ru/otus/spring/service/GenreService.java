package ru.otus.spring.service;

import ru.otus.spring.domain.BookGenre;
import ru.otus.spring.domain.Genre;

import java.util.Collection;
import java.util.List;

public interface GenreService {
    List<Genre> findGenres(String name);
    List<Genre> getGenres(List<String> name);
    void bindGenres(Long bookId, List<Genre> genres);
    List<BookGenre> getGenresByBookIds(Collection<Long> bookIds);

    void clearGenresBook(Long bookId);
}
