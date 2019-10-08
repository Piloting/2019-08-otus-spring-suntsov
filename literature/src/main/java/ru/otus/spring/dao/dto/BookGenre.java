package ru.otus.spring.dao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.spring.domain.Genre;

@Data
@AllArgsConstructor
public class BookGenre {
    private Long bookId;
    private Genre genre;
}
