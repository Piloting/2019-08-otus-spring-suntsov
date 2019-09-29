package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.BookGenre;
import ru.otus.spring.domain.Genre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService{
    
    private final GenreDao genreDao;
    
    @Override
    public List<Genre> findGenresByName(String name){
        if (!StringUtils.isEmpty(name)){
            return genreDao.findGenresByName(name);
        } else {
            return genreDao.findAllGenres();
        }
    }

    @Override
    public List<Genre> getGenresByNames(List<String> names) {
        if (CollectionUtils.isEmpty(names)){
            return null;
        }
        List<Genre> genreList = new ArrayList<>();
        for (String name : names) {
            List<Genre> genres = genreDao.findGenresByName(name);
            if (CollectionUtils.isEmpty(genres)){
                throw new RuntimeException("Not found genre by name: " + name);
            }
            genreList.addAll(genres);
        }
        return genreList;
    }

    @Override
    public void bindGenres(Long bookId, List<Genre> genres) {
        if (CollectionUtils.isEmpty(genres)){
            return;
        }
        List<BookGenre> bookGenres = genres.stream().map(genre -> new BookGenre(bookId, genre)).collect(Collectors.toList());
        genreDao.insertBookGenres(bookGenres);
    }

    @Override
    public List<BookGenre> getGenresByBookIds(Collection<Long> bookIds) {
        return genreDao.getGenresByBookIds(bookIds);
    }

    @Override
    public void clearGenresBook(Long bookId) {
        genreDao.deleteBookGenreByBookId(bookId);
    }
}
