package com.example.demo.mapper;

import com.example.demo.dto.BookDto;
import com.example.demo.model.Book;
import com.google.gson.Gson;

import java.util.Arrays;

public class BookMapper {

    private static Gson gson = new Gson();

    public static BookDto mapToBookDto(Book book) {
        return new BookDto(book.getRawid(),
                book.getTitle(),
                book.getAuthor(),
                book.getYear(),
                book.getPrice(),
                Arrays.stream(book.getGenres()).toList());
    }

    public static Book mapToBook(BookDto bookDto) {
        return new Book(bookDto.getId(),
                bookDto.getTitle(),
                bookDto.getAuthor(),
                bookDto.getYear(),
                bookDto.getPrice(),
                bookDto.getGenres().toArray(new String[0]));
    }
}
