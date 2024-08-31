package com.example.demo.repository;

import com.example.demo.model.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class BookRepository {
    private final List<Book> books = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public List<Book> findAll() {
        return new ArrayList<>(books);
    }

    public Optional<Book> findById(Long id) {
            return books.stream().filter(book -> book.getId().equals(id)).findFirst();
    }

    public Optional<Book> findByTitle(String title) {
        return books.stream().filter(book -> book.getTitle().equalsIgnoreCase(title)).findFirst();
    }

    public Book save(Book book) {
        if (book.getId() == null) {
            book.setId(nextId.getAndIncrement());
            books.add(book);
        } else {
            books.set(books.indexOf(book), book);
        }
        return book;
    }

    public void deleteById(Long id) {
        books.removeIf(book -> book.getId().equals(id));
    }

    public List<Book> findByFilter(String author, Integer priceBiggerThan, Integer priceLessThan, Integer yearBiggerThan, Integer yearLessThan, List<String> genres) {
        return books.stream()
                .filter(book -> (author == null || book.getAuthor().equalsIgnoreCase(author)) &&
                        (priceBiggerThan == null || book.getPrice() >= priceBiggerThan) &&
                        (priceLessThan == null || book.getPrice() <= priceLessThan) &&
                        (yearBiggerThan == null || book.getYear() >= yearBiggerThan) &&
                        (yearLessThan == null || book.getYear() <= yearLessThan) &&
                        (genres == null || book.getGenres().stream().anyMatch(genres::contains)))
                .collect(Collectors.toList());
    }
}
