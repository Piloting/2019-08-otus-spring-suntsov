package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookGenre;
import ru.otus.spring.domain.BookInfo;
import ru.otus.spring.domain.Genre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final AuthorService authorService;
    private final GenreService genreService;

    @Override
    public List<Book> getBooks(String title, String authorBrief, String genreName) {
        if (!StringUtils.isEmpty(title) || !StringUtils.isEmpty(title) || !StringUtils.isEmpty(title)){
            return bookDao.getByParam(title, authorBrief, genreName);
        } else {
            return bookDao.getAll();
        }
    }

    @Override
    public List<BookInfo> getBooksInfo(String title, String authorBrief, String genreName) {
        List<Book> books = getBooks(title, authorBrief, genreName);
        if (CollectionUtils.isEmpty(books)){
            return null;
        }
        
        // авторы
        Set<Long> authorIds = books.stream().map(Book::getAuthorId).collect(Collectors.toSet());
        List<Author> authors = authorService.getAuthorByIds(authorIds);
        Map<Long, Author> authorIdToAuthorMap = authors.stream().collect(Collectors.toMap(Author::getId, author -> author));

        // жанры
        Set<Long> bookIds = books.stream().map(Book::getId).collect(Collectors.toSet());
        List<BookGenre> bookGenreList = genreService.getGenresByBookIds(bookIds);
        Map<Long, List<Genre>> bookIdToGenresMap = new HashMap<>();
        for (BookGenre bookGenre : bookGenreList) {
            List<Genre> genres = bookIdToGenresMap.computeIfAbsent(bookGenre.getBookId(), k -> new ArrayList<>());
            genres.add(bookGenre.getGenre());
        }
        
        // итог
        List<BookInfo> bookInfos = new ArrayList<>(books.size());
        for (Book book : books) {
            BookInfo info = new BookInfo();
            info.setBook(book);
            info.setAuthor(authorIdToAuthorMap.get(book.getAuthorId()));
            info.setGenres(bookIdToGenresMap.get(book.getId()));
            bookInfos.add(info);
        }
        
        return bookInfos;
    }

    @Override
    public Book getById(Long id) {
        return null;
    }

    @Override
    public Book getByName(String name) {
        return null;
    }


    @Override
    public void addBook(String name, LocalDate releaseDate, String author) {

    }

    @Override
    public void addBookGenre(Book book, Genre genre) {

    }

    @Override
    public void deleteBookGenre(Book book, Genre genre) {

    }

    @Override
    public Long insertBook(Book book, List<Genre> genres) {
        Long bookId = bookDao.insertBook(book);
        book.setId(bookId);
        genreService.bindGenres(bookId, genres);
        return bookId;
    }

    @Override
    public void updateBook(Book book) {
        bookDao.updateBook(book);
    }

    @Override
    public void deleteBook(Long bookId) {
        genreService.clearGenresBook(bookId);
        bookDao.deleteBook(bookId);
    }
}
