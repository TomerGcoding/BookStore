package com.example.demo.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class BookController {
    @Autowired
    private BookService bookService;
    private static final List<String> validLevels = List.of("ERROR", "INFO", "DEBUG");
    private static final List<String> validLoggers = List.of("request-logger", "books-logger");

    private static final Logger requestLogger = LoggerFactory.getLogger("request-logger");
    private static final Logger booksLogger = LoggerFactory.getLogger("books-logger");
    private static final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    private static long requestCounter = 1;

    @GetMapping("/books/health")
    public ResponseEntity<String> healthCheck() {
        long requestId = requestCounter++;
        long startTime = System.currentTimeMillis();
        requestLogger.info("Incoming request | #{} | resource: /books/health | HTTP Verb GET | request #{}", requestId,requestId);
        ResponseEntity<String> response = ResponseEntity.ok("OK");
        long endTime = System.currentTimeMillis();
        requestLogger.debug("request #{} duration: {}ms | request #{}", requestId, endTime - startTime,requestId);
        return response;
    }

    @PostMapping("/book")
    public ResponseEntity<Map<String, Object>> createBook(@RequestBody Book book) {
        long requestId = requestCounter++;
        long startTime = System.currentTimeMillis();
        requestLogger.info("Incoming request | #{} | resource: /book | HTTP Verb POST | request #{}", requestId,requestId);
        try {
            Book createdBook = bookService.addBook(book);
            booksLogger.info("Creating new Book with Title [{}] | request #{}", book.getTitle(),requestId);
            booksLogger.debug("Currently there are {} Books in the system. New Book will be assigned with id {} | request #{}",
                    bookService.getAllBooks().size()-1, createdBook.getId(), requestId);
            ResponseEntity<Map<String, Object>> response = ResponseEntity.ok(Map.of("result", createdBook.getId()));
            long endTime = System.currentTimeMillis();
            requestLogger.debug("request #{} duration: {}ms | request #{}", requestId, endTime - startTime,requestId);
            return response;
        } catch (Exception e) {
            booksLogger.error("{} | request #{}", e.getMessage(), requestId);
            long endTime = System.currentTimeMillis();
            requestLogger.debug("request #{} duration: {}ms | request #{}", requestId, endTime - startTime,requestId);
            return ResponseEntity.status(409).body(Map.of("errorMessage", e.getMessage()));
        }
    }

    @GetMapping("/books/total")
    public ResponseEntity<Map<String, Object>> getTotalBooks(
            @RequestParam(name="author",required = false) String author,
            @RequestParam(name="price-bigger-than",required = false) Integer priceBiggerThan,
            @RequestParam(name="price-less-than",required = false) Integer priceLessThan,
            @RequestParam(name="year-bigger-than",required = false) Integer yearBiggerThan,
            @RequestParam(name="year-less-than",required = false) Integer yearLessThan,
            @RequestParam(name="genres",required = false) String genres
    ) {
        long requestId = requestCounter++;
        long startTime = System.currentTimeMillis();
        requestLogger.info("Incoming request | #{} | resource: /books/total | HTTP Verb GET | request #{}", requestId,requestId);

        List<String> genreList = null;
        List<String> allGenres = List.of("SCI_FI", "NOVEL", "MANGA", "HISTORY", "ROMANCE", "PROFESSIONAL");
        if (genres != null) {
            genreList = Arrays.asList(genres.split(","));
            for (String genre : genreList)
                if (!allGenres.contains(genre))
                    return ResponseEntity.status(400).build();
        }

        List<Book> filteredBooks = bookService.getBooksByFilter(author, priceBiggerThan, priceLessThan, yearBiggerThan, yearLessThan, genreList);
        booksLogger.info("Total Books found for requested filters is {} | request #{}", filteredBooks.size(), requestId);
        ResponseEntity<Map<String, Object>> response = ResponseEntity.ok(Map.of("result", filteredBooks.size()));
        long endTime = System.currentTimeMillis();
        requestLogger.debug("request #{} duration: {}ms | request #{}", requestId, endTime - startTime,requestId);
        return response;
    }

    @GetMapping("/books")
    public ResponseEntity<Map<String, List<Book>>> getBooks(
            @RequestParam(name="author",required = false) String author,
            @RequestParam(name="price-bigger-than",required = false) Integer priceBiggerThan,
            @RequestParam(name="price-less-than",required = false) Integer priceLessThan,
            @RequestParam(name="year-bigger-than",required = false) Integer yearBiggerThan,
            @RequestParam(name="year-less-than",required = false) Integer yearLessThan,
            @RequestParam(name="genres",required = false) String genres
    ) {
        long requestId = requestCounter++;
        long startTime = System.currentTimeMillis();
        requestLogger.info("Incoming request | #{} | resource: /books | HTTP Verb GET | request #{}", requestId,requestId);

        List<String> genreList = null;
        List<String> allGenres = List.of("SCI_FI", "NOVEL", "MANGA", "HISTORY", "ROMANCE", "PROFESSIONAL");
        if (genres != null) {
            genreList = Arrays.asList(genres.split(","));
            for (String genre : genreList)
                if (!allGenres.contains(genre))
                    return ResponseEntity.status(400).build();
        }
        List<Book> filteredBooks = bookService.getBooksByFilter(author, priceBiggerThan, priceLessThan, yearBiggerThan, yearLessThan, genreList);
        filteredBooks.sort((b1, b2) -> b1.getTitle().compareTo(b2.getTitle()));
        booksLogger.info("Total Books found for requested filters is {} | request #{}", filteredBooks.size(), requestId);
        ResponseEntity<Map<String, List<Book>>> response = ResponseEntity.ok(Map.of("result", filteredBooks));
        long endTime = System.currentTimeMillis();
        requestLogger.debug("request #{} duration: {}ms | request #{}", requestId, endTime - startTime,requestId);
        return response;
    }

    @GetMapping("/book")
    public ResponseEntity<Map<String, Object>> getBookById(@RequestParam(name = "id") Long id) {
        long requestId = requestCounter++;
        long startTime = System.currentTimeMillis();
        requestLogger.info("Incoming request | #{} | resource: /book | HTTP Verb GET | request #{}", requestId,requestId);

        try {
            Book book = bookService.getBookById(id).orElseThrow(() -> new Exception("Error: no such Book with id " + id));
            booksLogger.debug("Fetching book id {} details | request #{}", id, requestId);
            ResponseEntity<Map<String, Object>> response = ResponseEntity.ok(Map.of("result", book));
            long endTime = System.currentTimeMillis();
            requestLogger.debug("request #{} duration: {}ms | request #{}", requestId, endTime - startTime,requestId);
            return response;
        } catch (Exception e) {
            booksLogger.error("{} | request #{}", e.getMessage(), requestId);
            long endTime = System.currentTimeMillis();
            requestLogger.debug("request #{} duration: {}ms | request #{}", requestId, endTime - startTime,requestId);
            return ResponseEntity.status(404).body(Map.of("errorMessage", e.getMessage()));
        }
    }

    @PutMapping("/book")
    public ResponseEntity<?> updateBookPrice(@RequestParam(name = "id") Long id, @RequestParam(name = "price") int price) {
        long requestId = requestCounter++;
        long startTime = System.currentTimeMillis();
        requestLogger.info("Incoming request | #{} | resource: /book | HTTP Verb PUT | request #{}", requestId,requestId);

        try {
            int oldPrice = bookService.updateBookPrice(id, price);
            booksLogger.info("Update Book id [{}] price to {} | request #{}", id, price, requestId);
            Book book = bookService.getBookById(id).get();
            booksLogger.debug("Book [{}] price change: {} --> {} | request #{}", book.getTitle(), oldPrice, price, requestId);
            ResponseEntity<?> response = ResponseEntity.ok(Map.of("result", oldPrice));
            long endTime = System.currentTimeMillis();
            requestLogger.debug("request #{} duration: {}ms | request #{}", requestId, endTime - startTime,requestId);
            return response;
        } catch (Exception e) {
            booksLogger.error("{} | request #{}", e.getMessage(), requestId);
            long endTime = System.currentTimeMillis();
            requestLogger.debug("request #{} duration: {}ms | request #{}", requestId, endTime - startTime,requestId);
            int status;
            if (price <= 0)
                status = 409;
            else
                status = 404;
            return ResponseEntity.status(status).body(Map.of("errorMessage", e.getMessage()));
        }
    }

    @DeleteMapping("/book")
    public ResponseEntity<?> deleteBook(@RequestParam(name = "id") Long id) {
        long requestId = requestCounter++;
        long startTime = System.currentTimeMillis();
        requestLogger.info("Incoming request | #{} | resource: /book | HTTP Verb DELETE | request #{}", requestId,requestId);

        try {
            Book book = bookService.getBookById(id).get();
            bookService.deleteBook(id);
            booksLogger.info("Removing book [{}] | request #{}", book.getTitle(), requestId);
            booksLogger.debug("After removing book [{}] id: [{}] there are {} books in the system | request #{}",
                    book.getTitle(), id, bookService.getAllBooks().size(), requestId);
            ResponseEntity<?> response = ResponseEntity.ok(Map.of("result", bookService.getAllBooks().size()));
            long endTime = System.currentTimeMillis();
            requestLogger.debug("request #{} duration: {}ms | request #{}", requestId, endTime - startTime,requestId);
            return response;
        } catch (Exception e) {
            booksLogger.error("{} | request #{}", e.getMessage(), requestId);
            long endTime = System.currentTimeMillis();
            requestLogger.debug("request #{} duration: {}ms | request #{}", requestId, endTime - startTime,requestId);
            return ResponseEntity.status(404).body(Map.of("errorMessage", e.getMessage()));
        }
    }

    @GetMapping("/logs/level")
    public ResponseEntity<String> getLogLevel(@RequestParam(name="logger-name") String loggerName) {
        long requestId = requestCounter++;
        long startTime = System.currentTimeMillis();
        requestLogger.info("Incoming request | #{} | resource: /logs/level | HTTP Verb GET | request #{}", requestId,requestId);
        try {
            if (!validLoggers.contains(loggerName))
                throw new Exception(String.format("Couldnt find a logger named %s", loggerName));
            Level logLevel = loggerContext.getLogger(loggerName).getLevel();
            long endTime = System.currentTimeMillis();
            requestLogger.debug("request #{} duration: {}ms | request #{}", requestId, endTime - startTime, requestId);
            return ResponseEntity.ok(String.format("%s", logLevel.levelStr));
        }
        catch (Exception e) {
            requestLogger.error("{} | request #{}", e.getMessage(), requestId);
            long endTime = System.currentTimeMillis();
            requestLogger.debug("request #{} duration: {}ms | request #{}", requestId, endTime - startTime,requestId);
            return ResponseEntity.status(400).body(String.format("Failure: %s", e.getMessage()));
        }
    }

    @PutMapping("/logs/level")
    public ResponseEntity<String> setLogLevel(@RequestParam(name="logger-name") String loggerName, @RequestParam(name="logger-level") String logLevel) {
        long requestId = requestCounter++;
        long startTime = System.currentTimeMillis();
        requestLogger.info("Incoming request | #{} | resource: /logs/level | HTTP Verb PUT | request #{}", requestId,requestId);

        try {
            if(!validLevels.contains(logLevel.toUpperCase()))
                throw new Exception(String.format("%s is not a valid level",logLevel));
            if(!validLoggers.contains(loggerName))
                throw new Exception(String.format("Couldnt find a logger named %s",loggerName));
            loggerContext.getLogger(loggerName).setLevel(Level.toLevel(logLevel));
            long endTime = System.currentTimeMillis();
            requestLogger.debug("request #{} duration: {}ms | request #{}", requestId, endTime - startTime,requestId);
            return ResponseEntity.ok(String.format("%s", logLevel.toUpperCase()));
        } catch (Exception e) {
            requestLogger.error("{} | request #{}", e.getMessage(), requestId);
            long endTime = System.currentTimeMillis();
            requestLogger.debug("request #{} duration: {}ms | request #{}", requestId, endTime - startTime,requestId);
            return ResponseEntity.status(400).body(String.format("%s", e.getMessage()));
        }
    }
}
