package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.otus.spring.dao.dto.Book;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.BookInfo;
import ru.otus.spring.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BookDaoImpl implements BookDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final GenreDao genreDao;
    private final AuthorDao authorDao;
    
    @Override
    public List<BookInfo> getByParam(String title, String authorBrief, String genreName) {
        Map<String, Object> params = new HashMap<>(3);
        putIsNeed(params, "TITLE", title);
        putIsNeed(params, "AUTHORBRIEF", authorBrief);
        putIsNeed(params, "GENRENAME", genreName);
        
        String query = 
                "SELECT BOOK.ID, BOOK.TITLE, BOOK.AUTHORID " +
                "FROM BOOK " +
                "LEFT JOIN AUTHOR ON AUTHOR.ID = BOOK.AUTHORID " +
                "WHERE 1=1 " +
                addIsNeed(title,       "AND BOOK.TITLE   LIKE :TITLE ") +
                addIsNeed(authorBrief, "AND AUTHOR.BRIEF LIKE :AUTHORBRIEF ") +
                addIsNeed(genreName,   "AND EXISTS(SELECT 1 " +
                                                "           FROM BOOKGENRE " +
                                                "           JOIN GENRE ON GENRE.ID = BOOKGENRE.GENREID" +
                                                "           WHERE BOOKGENRE.BOOKID = BOOK.ID " +
                                                "           AND GENRE.NAME LIKE :GENRENAME)");
        List<Book> books = namedParameterJdbcOperations.query(query, params, new BookMapper());
        
        return fillBookInfoParam(books);
    }

    private String addIsNeed(String param, String subQuery) {
        return StringUtils.isEmpty(param) ? "" : subQuery;
    }
    private void putIsNeed(Map<String, Object> params, String name, String value) {
        if (StringUtils.isEmpty(value)){
            return;
        }
        params.put(name, value);
    }

    @Override
    public List<BookInfo> getAll() {
        List<Book> books = namedParameterJdbcOperations.query(
                "SELECT ID, TITLE, AUTHORID FROM BOOK", new BookMapper()
        );

        if (CollectionUtils.isEmpty(books)){
            return new ArrayList<>();
        }
        
        return fillBookInfoParam(books);
    }

    private List<BookInfo> fillBookInfoParam(List<Book> books) {
        List<BookInfo> bookInfos = new ArrayList<>(books.size());
        
        List<Long> bookIds = books.stream().map(Book::getId).collect(Collectors.toList());
        Set<Long> authorIds = books.stream().map(Book::getAuthorId).collect(Collectors.toSet());

        List<Author> authors = authorDao.getAuthorByIds(authorIds);
        Map<Long, Author> authorMap = authors.stream().collect(Collectors.toMap(Author::getId, a -> a));

        Map<Long, List<Genre>> genresByBookIds = genreDao.getGenresByBookIds(bookIds);


        for (Book book : books) {
            BookInfo bookInfo = new BookInfo();
            bookInfo.setId(book.getId());
            bookInfo.setTitle(book.getTitle());
            bookInfo.setAuthor(authorMap.get(book.getAuthorId()));
            bookInfo.setGenres(genresByBookIds.get(book.getId()));
            bookInfos.add(bookInfo);
        }
        return bookInfos;
    }


    @Override
    public Book getById(Long id) {
        Map<String, Object> params = Collections.singletonMap("ID", id);
        return namedParameterJdbcOperations.queryForObject(
                "SELECT ID, TITLE, AUTHORID FROM BOOK WHERE ID = :ID", params, new BookMapper()
        );
    }

    @Override
    public Long insertBook(BookInfo bookInfo) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("TITLE", bookInfo.getTitle());
        params.put("AUTHORID", bookInfo.getAuthor() != null ? bookInfo.getAuthor().getId() : null);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update("INSERT INTO BOOK (TITLE, AUTHORID) VALUES (:TITLE, :AUTHORID)", new MapSqlParameterSource(params), keyHolder);
        bookInfo.setId(keyHolder.getKey().longValue());
        return bookInfo.getId();
    }

    @Override
    public void updateBook(BookInfo bookInfo) {
        Book existBook = getById(bookInfo.getId());

        Map<String, Object> params = new HashMap<>();
        params.put("ID", bookInfo.getId());
        params.put("TITLE", bookInfo.getTitle() != null ? bookInfo.getTitle() : existBook.getTitle());
        params.put("AUTHORID", bookInfo.getAuthor() != null ? bookInfo.getAuthor().getId() : existBook.getAuthorId());
        namedParameterJdbcOperations.update(
                "UPDATE BOOK " +
                   "SET TITLE    = :TITLE, " +
                   "    AUTHORID = :AUTHORID " +
                   "WHERE ID = :ID", params);
    }

    @Override
    public void deleteBook(Long bookId) {
        Map<String, Object> params = Collections.singletonMap("ID", bookId);
        namedParameterJdbcOperations.update("DELETE FROM BOOK WHERE ID = :ID", params);
    }
    
    private static class BookMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            Long id = resultSet.getLong("ID");
            String title = resultSet.getString("TITLE");
            Long authorId = resultSet.getLong("AUTHORID");
            return new Book(id, title, authorId);
        }
    }
}
