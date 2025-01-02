package com.example.demo.repository.mongo;

import com.example.demo.model.Book;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MongoBookRepository extends MongoRepository<Book, String> {

    boolean existsByTitle(String title);

    Optional<Book> findByRawid(Integer rawid);


    boolean existsByRawid(Integer rawid);

    void deleteByRawid(Integer rawid);




}
