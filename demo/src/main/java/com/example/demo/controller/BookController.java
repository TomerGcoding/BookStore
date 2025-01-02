package com.example.demo.controller;


import com.example.demo.dto.BookDto;
import com.example.demo.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
public class BookController {

    private BookService bookService;

    @GetMapping("/books/health")
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/book")
    public ResponseEntity<Map<String,?>> createBook(@RequestBody BookDto bookDto) {
        try {
            BookDto createdBook = bookService.createBook(bookDto);
            return new ResponseEntity<>(Map.of("result", createdBook.getId()), HttpStatus.CREATED);
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Map.of("errorMessage",e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/books/total")
    public ResponseEntity<Map<String,?>> totalBooksWithFilters(@RequestParam(name = "author",required = false) String author,
                                                               @RequestParam(name = "price-bigger-than",required = false) Integer priceBiggerThan,
                                                               @RequestParam(name = "price-less-than",required = false) Integer priceLessThan,
                                                               @RequestParam(name = "year-bigger-than",required = false) Integer yearBiggerThan,
                                                               @RequestParam(name = "year-less-than",required = false) Integer yearLessThan,
                                                               @RequestParam(name = "genres",required = false) String genres,
                                                               @RequestParam(name = "persistenceMethod",required = false) String persistenceMethod) {

        return new ResponseEntity<>(Map.of("result", bookService.findByFilter(author,priceBiggerThan,priceLessThan,yearBiggerThan,yearLessThan,genres,persistenceMethod).size()), HttpStatus.OK);
    }


    @GetMapping("/books")
    public ResponseEntity<Map<String,?>> listBooksWithFilters(@RequestParam(name = "author",required = false) String author,
                                                                     @RequestParam(name = "price-bigger-than",required = false) Integer priceBiggerThan,
                                                                     @RequestParam(name = "price-less-than",required = false) Integer priceLessThan,
                                                                     @RequestParam(name = "year-bigger-than",required = false) Integer yearBiggerThan,
                                                                     @RequestParam(name = "year-less-than",required = false) Integer yearLessThan,
                                                                     @RequestParam(name = "genres",required = false) String genres,
                                                                     @RequestParam(name = "persistenceMethod",required = false) String persistenceMethod) {

        return new ResponseEntity<>(Map.of("result", bookService.findByFilter(author,priceBiggerThan,priceLessThan,yearBiggerThan,yearLessThan,genres,persistenceMethod)), HttpStatus.OK);
    }


    @PutMapping("/book")
    public ResponseEntity<Map<String,?>> updateBookPrice(@RequestParam(name = "id") Integer id,
                                                         @RequestParam(name = "price") Integer price) {
        try {
            BookDto updatedBook = bookService.updatePrice(id,price);
            return new ResponseEntity<>(Map.of("result", updatedBook.getPrice()), HttpStatus.OK);
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Map.of("errorMessage",e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/book")
    public ResponseEntity<Map<String,?>> getBookById(@RequestParam(name = "id") Integer id,
                                                     @RequestParam(name = "persistenceMethod",required = false) String persistenceMethod) {
        try {
            BookDto book = bookService.findById(id,persistenceMethod);
            return new ResponseEntity<>(Map.of("result", book), HttpStatus.OK);
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Map.of("errorMessage",e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/book")
    public ResponseEntity<Map<String,?>> deleteBook(@RequestParam(name = "id") Integer id) {
        try {
            bookService.deleteBook(id);
            return new ResponseEntity<>(Map.of("result",bookService.findAll().size()), HttpStatus.OK);
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Map.of("errorMessage",e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
