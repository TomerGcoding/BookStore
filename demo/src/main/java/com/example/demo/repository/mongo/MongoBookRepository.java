package com.example.demo.repository.mongo;

import com.example.demo.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository("mongoBookRepository")
public interface MongoBookRepository extends MongoRepository<Book, Long> {
}
