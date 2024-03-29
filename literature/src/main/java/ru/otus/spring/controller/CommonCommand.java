package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.util.StringUtils;
import ru.otus.spring.domain.Author;
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
        return genreService.findGenresByName(name);
    }
    
    @ShellMethod(value = "Get authors", key = {"a", "authors"})
    public List<Author> getAuthors(@ShellOption(defaultValue = "", value = {"b"}) String brief) {
        return authorService.findAuthorsByBrief(brief);
    }

    /**
     * Example: 
     *   b
     *   b t Рус% a Пуш% g Сказка
     */
    @ShellMethod(value = "Get books", key = {"b", "books"})
    public List<BookInfo> getBooks(
            @ShellOption(defaultValue = "", value = {"t"}, arity = 1) String title,
            @ShellOption(defaultValue = "", value = {"a"}, arity = 1) String author,
            @ShellOption(defaultValue = "", value = {"g"}, arity = 1) String genre) {
        return bookService.getBooksByParam(title, author, genre);
    }

    /**
     * Example: 
     *   bc t Заголовок ab Пуш%
     *   bc t Заголовок ab Пуш% g Фан%,Сказ%
     */
    @ShellMethod(value = "Create book", key = {"bc"})
    public Long createBook(
            @ShellOption(                   value = {"t"},  arity = 1) String title,
            @ShellOption(defaultValue = "", value = {"ab"}, arity = 1) String authorBrief,
            @ShellOption(defaultValue = "", value = {"ai"}, arity = 1) Long   authorId,
            @ShellOption(defaultValue = "", value = {"g"}            ) List<String> genres
    ){
        if (StringUtils.isEmpty(title)){
            System.out.println("Ошибка: необходимо задать название книги!");
            return null;
        }
        if (StringUtils.isEmpty(authorBrief) && authorId == null){
            System.out.println("Ошибка: необходимо задать автора!");
            return null;
        }

        Author author = authorService.getAuthor(authorId, authorBrief);
        List<Genre> genresList = genreService.getGenresByNames(genres);

        BookInfo bookInfo = new BookInfo();
        bookInfo.setTitle(title);
        bookInfo.setAuthor(author);
        bookInfo.setGenres(genresList);
        bookService.insertBook(bookInfo);
        System.out.println("Book created");
        return bookInfo.getId();
    }

    /**
     * Example: bu id 2 t Привет
     */
    @ShellMethod(value = "Update book", key = {"bu"})
    public void updateBook(
            @ShellOption(                   value = {"id"}, arity = 1) Long   id,
            @ShellOption(defaultValue = "", value = {"t"},  arity = 1) String title,
            @ShellOption(defaultValue = "", value = {"ab"}, arity = 1) String authorBrief,
            @ShellOption(defaultValue = "", value = {"ai"}, arity = 1) Long   authorId
    ){
        Author author = null;
        if (authorId != null || !StringUtils.isEmpty(authorBrief)){
            author = authorService.getAuthor(authorId, authorBrief);
        }
        BookInfo bookInfo = new BookInfo();
        bookInfo.setId(id);
        bookInfo.setTitle(title);
        bookInfo.setAuthor(author);
        bookService.updateBook(bookInfo);
        System.out.println("Book updated");
    }

    /**
     * Example: bd 1
     */
    @ShellMethod(value = "Delete book", key = {"bd"})
    public void deleteBook(Long bookId){
        bookService.deleteBook(bookId);
        System.out.println("Book deleted");
    }
}
