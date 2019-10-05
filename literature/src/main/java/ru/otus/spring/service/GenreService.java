package ru.otus.spring.service;

import ru.otus.spring.domain.Genre;
import java.util.List;

public interface GenreService {
    List<Genre> findGenresByName(String name);
    List<Genre> getGenresByNames(List<String> name);
    void bindGenres(Long bookId, List<Genre> genres);

    void clearGenresBook(Long bookId);
}
