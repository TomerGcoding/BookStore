package com.example.demo.repository.postgres;

import com.example.demo.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PostgresBookRepository extends JpaRepository<Book, Integer> {

        boolean existsByTitle(String title);

        Optional<Book> findByTitle(String title);
}
