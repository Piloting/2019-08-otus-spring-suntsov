package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import ru.otus.spring.domain.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class BookDaoImpl implements BookDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    
    @Override
    public List<Book> getByParam(String title, String authorBrief, String genreName) {
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
        
        return namedParameterJdbcOperations.query(query, params, new BookMapper());
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
    public List<Book> getAll() {
        return namedParameterJdbcOperations.query(
                "SELECT ID, TITLE, AUTHORID FROM BOOK", new BookMapper()
        );
    }


    @Override
    public Book getById(Long id) {
        Map<String, Object> params = Collections.singletonMap("ID", id);
        return namedParameterJdbcOperations.queryForObject(
                "SELECT ID, TITLE, AUTHORID FROM BOOK WHERE ID = :ID", params, new BookMapper()
        );
    }

    @Override
    public Long insertBook(Book book) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("TITLE", book.getTitle());
        params.put("AUTHORID", book.getAuthorId());

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update("INSERT INTO BOOK (TITLE, AUTHORID) VALUES (:TITLE, :AUTHORID)", new MapSqlParameterSource(params), keyHolder);
        book.setId(keyHolder.getKey().longValue());
        return book.getId();
    }

    @Override
    public void updateBook(Book book) {
        Book existBook = getById(book.getId());

        Map<String, Object> params = new HashMap<>();
        params.put("ID", book.getId());
        params.put("TITLE", book.getTitle() != null ? book.getTitle() : existBook.getTitle());
        params.put("AUTHORID", book.getAuthorId() != null ? book.getAuthorId() : existBook.getAuthorId());
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
