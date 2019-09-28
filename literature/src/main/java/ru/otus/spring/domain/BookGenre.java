package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class BookGenre {
    private Long bookId;
    private Genre genre;
}
