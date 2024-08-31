package com.example.demo.service;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book addBook(Book book) throws Exception {
        if (bookRepository.findByTitle(book.getTitle()).isPresent()) {
            throw new Exception("Error: Book with the title [" + book.getTitle() + "] already exists in the system");
        }
        if (book.getYear() < 1940 || book.getYear() > 2100) {
            throw new Exception("Error: Can't create new Book that its year [" + book.getYear() + "] is not in the accepted range [1940 -> 2100]");
        }
        if (book.getPrice() <= 0) {
            throw new Exception("Error: Can't create new Book with negative price");
        }
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) throws Exception {
        if (!bookRepository.findById(id).isPresent()) {
            throw new Exception("Error: no such Book with id " + id);
        }
        bookRepository.deleteById(id);
    }

    public List<Book> getBooksByFilter(String author, Integer priceBiggerThan, Integer priceLessThan, Integer yearBiggerThan, Integer yearLessThan, List<String> genres) {
        return bookRepository.findByFilter(author, priceBiggerThan, priceLessThan, yearBiggerThan, yearLessThan, genres);
    }

    public int updateBookPrice(Long id, int newPrice) throws Exception {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (!bookOptional.isPresent()) {
            throw new Exception("Error: no such Book with id " + id);
        }
        if (newPrice <= 0) {
            throw new Exception("Error: price update for book [" + id + "] must be a positive integer");
        }
        int oldPrice = bookOptional.get().getPrice();
        Book book = bookOptional.get();
        book.setPrice(newPrice);
        bookRepository.save(book);
        return oldPrice;
    }
}
