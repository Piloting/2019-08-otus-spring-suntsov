package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AuthorDaoImpl implements AuthorDao{
    private static final int SPLIT_SIZE = 1000;
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public List<Author> findAuthorsByBrief(String brief){
        Map<String, Object> params = Collections.singletonMap("BRIEF", brief);
        return namedParameterJdbcOperations.query(
                "SELECT ID, BRIEF FROM AUTHOR WHERE BRIEF LIKE :BRIEF", params, new AuthorMapper()
        );
    }
    
    public List<Author> findAllAuthors(){
        return namedParameterJdbcOperations.query(
                "SELECT ID, BRIEF FROM AUTHOR", new AuthorMapper()
        );
    }
    
    public List<Author> getAuthorByIds(Collection<Long> ids){
        List<Author> authors = new ArrayList<>(ids.size());

        Collection<List<Long>> slittedIds = ids.stream().collect(Collectors.groupingBy(s -> (s - 1) / SPLIT_SIZE)).values();
        Map<String, Object> params;
        for (List<Long> slittedId : slittedIds) {
            params = Collections.singletonMap("IDS", new HashSet<>(slittedId));
            authors.addAll(namedParameterJdbcOperations.query(
                    "SELECT ID, BRIEF FROM AUTHOR WHERE ID IN (:IDS)", params, new AuthorMapper()
            ));
        }

        return authors;
    }
    
    
    
    private static class AuthorMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            Long id = resultSet.getLong("ID");
            String brief = resultSet.getString("BRIEF");
            return new Author(id, brief);
        }
    }
}
