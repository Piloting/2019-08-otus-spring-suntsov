package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.domain.BookInfo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final GenreService genreService;

    @Override
    public List<BookInfo> getBooksByParam(String title, String authorBrief, String genreName) {
        if (!StringUtils.isEmpty(title) || !StringUtils.isEmpty(title) || !StringUtils.isEmpty(title)){
            return bookDao.getByParam(title, authorBrief, genreName);
        } else {
            return bookDao.getAll();
        }
    }

    @Override
    public Long insertBook(BookInfo bookInfo) {
        Long bookId = bookDao.insertBook(bookInfo);
        bookInfo.setId(bookId);
        genreService.bindGenres(bookId, bookInfo.getGenres());
        return bookId;
    }

    @Override
    public void updateBook(BookInfo bookInfo) {
        bookDao.updateBook(bookInfo);
    }

    @Override
    public void deleteBook(Long bookId) {
        genreService.clearGenresBook(bookId);
        bookDao.deleteBook(bookId);
    }
}
