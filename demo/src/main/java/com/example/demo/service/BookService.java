package com.example.demo.service;

import com.example.demo.model.Book;
import com.example.demo.repository.mongo.MongoBookRepository;
import com.example.demo.repository.postgres.PostgresBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class BookService {

    private static final String POSTGRES = "POSTGRES";
    private static final String MONGO = "MONGO";

    @Autowired
    private PostgresBookRepository postgresBookRepository;

    @Autowired
    private MongoBookRepository mongoBookRepository;

    public Book saveBook(Book book) {
        if(postgresBookRepository.findByTitle(book.getTitle()).isPresent()){
            return null;
        }
        book.setId(getBooksCount(POSTGRES) + 1L);
        Book savedBook = postgresBookRepository.save(book);
        mongoBookRepository.save(book); // Ensure MongoDB has the same data
        return savedBook;
    }

    public Optional<Book> getBookById(Long id, String presistenceMethod){
        if(presistenceMethod.equals(POSTGRES)){
            return postgresBookRepository.findById(id);
        }else if(presistenceMethod.equals(MONGO)){
            return mongoBookRepository.findById(id);
        }
        return Optional.empty();
    }

    public Integer getBooksCount(String presistenceMethod){
        if(presistenceMethod.equals(POSTGRES)){
            return (int) postgresBookRepository.count();
        }else if(presistenceMethod.equals(MONGO)){
            return (int) mongoBookRepository.count();
        }
        return 0;
    }



}
