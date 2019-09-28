package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.BookGenre;
import ru.otus.spring.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreDao{
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public List<Genre> findGenresByName(String name){
        Map<String, Object> params = Collections.singletonMap("NAME", name);
        return namedParameterJdbcOperations.query(
                "SELECT ID, NAME FROM GENRE WHERE NAME LIKE :NAME", params, new GenreMapper()
        );
    }
    
    public List<Genre> findAllGenres(){
        return namedParameterJdbcOperations.query(
                "SELECT ID, NAME FROM GENRE", new GenreMapper()
        );
    }

    @Override
    public List<BookGenre> getGenresByBookIds(Collection<Long> bookIds) {
        Map<String, Object> params = Collections.singletonMap("IDS", new HashSet<>(bookIds));
        return namedParameterJdbcOperations.query(
                "SELECT BOOKGENRE.BOOKID, BOOKGENRE.GENREID, GENRE.NAME " +
                        "FROM BOOKGENRE " +
                        "JOIN GENRE ON GENRE.ID = BOOKGENRE.GENREID " +
                        "WHERE BOOKGENRE.BOOKID IN (:IDS)", params, new BookGenreMapper()
        );
    }

    @Override
    public void insertBookGenres(List<BookGenre> bookGenres) {
        for (BookGenre bookGenre : bookGenres) {
            Map<String, Object> params = new HashMap<>();
            params.put("BOOKID", bookGenre.getBookId());
            params.put("GENREID", bookGenre.getGenre().getId());
            namedParameterJdbcOperations.update("INSERT INTO BOOKGENRE (BOOKID, GENREID) VALUES (:BOOKID, :GENREID)", params);
        }
    }

    @Override
    public void deleteBookGenreByBookId(Long bookId) {
        Map<String, Object> params = Collections.singletonMap("BOOKID", bookId);
        namedParameterJdbcOperations.update("DELETE FROM BOOKGENRE WHERE BOOKID = :BOOKID", params);
    }

    private static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            Long id = resultSet.getLong("ID");
            String name = resultSet.getString("NAME");
            return new Genre(id, name);
        }
    }
    
    private static class BookGenreMapper implements RowMapper<BookGenre> {
        @Override
        public BookGenre mapRow(ResultSet resultSet, int i) throws SQLException {
            Long bookId = resultSet.getLong("BOOKID");
            Long genreId = resultSet.getLong("GENREID");
            String name = resultSet.getString("NAME");
            return new BookGenre(bookId, new Genre(genreId, name));
        }
    }
}
