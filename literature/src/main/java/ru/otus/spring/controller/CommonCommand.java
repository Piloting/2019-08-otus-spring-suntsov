package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookInfo;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.GenreService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class CommonCommand {

    private final GenreService genreService;
    private final AuthorService authorService;
    private final BookService bookService;


    @ShellMethod(value = "Get genres", key = {"g", "genres"})
    public List<Genre> getGenres(@ShellOption(defaultValue = "", value = {"n"}) String name) {
        return genreService.findGenres(name);
    }
    
    @ShellMethod(value = "Get authors", key = {"a", "authors"})
    public List<Author> getAuthors(@ShellOption(defaultValue = "", value = {"b"}) String brief) {
        return authorService.findAuthors(brief);
    }


    
    
    
}
