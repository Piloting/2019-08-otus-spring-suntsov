package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.domain.Author;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService{
    
    private final AuthorDao authorDao;

    @Override
    public List<Author> findAuthors(String brief) {
        if (!StringUtils.isEmpty(brief)){
            return authorDao.findAuthorsByBrief(brief);
        } else {
            return authorDao.findAllAuthors();
        }
    }

    @Override
    public List<Author> getAuthorByIds(Collection<Long> ids) {
        return authorDao.getAuthorByIds(ids);
    }

    @Override
    public Author getAuthor(Long id, String brief) {
        List<Author> authors;
        if (id != null){
            authors = authorDao.getAuthorByIds(Collections.singleton(id));
        } else if (!StringUtils.isEmpty(brief)) {
            authors = authorDao.findAuthorsByBrief(brief);
        } else {
            throw new RuntimeException("Missing author param!");
        }
        
        if (authors.size() != 1){
            throw new RuntimeException("Could not find author by parameters: ID=" + id + ", BRIEF="+brief);
        }
        
        return authors.iterator().next();
    }
}
