package com.example.demo.repository.postgres;

import com.example.demo.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("postgresBookRepository")
public interface PostgresBookRepository extends JpaRepository<Book, Long> {
    public Optional<Book> findByTitle(String title);
}
