package com.example.demo.repository;

import com.example.demo.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoBookRepository extends MongoRepository<Book, Long> {
}
