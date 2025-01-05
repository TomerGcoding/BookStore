package com.example.demo.service.impl;

import com.example.demo.dto.BookDto;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.mongo.MongoBookRepository;
import com.example.demo.repository.postgres.PostgresBookRepository;
import com.example.demo.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private static final String MONGO_DB = "MONGO";
    private static final String POSTGRES_DB = "POSTGRES";

    private MongoBookRepository mongoBookRepository;

    private PostgresBookRepository postgresBookRepository;

    @Override
    public List<BookDto> findAll() {
        return postgresBookRepository.findAll().stream()
                .map(BookMapper::mapToBookDto)
                .toList();
    }

    @Override
    public List<BookDto> findByFilter(String author,
                                      Integer priceBiggerThan,
                                      Integer priceLessThan,
                                      Integer yearBiggerThan,
                                      Integer yearLessThan,
                                      String genres,
                                      String persistenceMethod) {
        List<String> genresList = genres != null ? List.of(genres.split(",")) : null;
        if(persistenceMethod.equals(POSTGRES_DB)) {
            return postgresBookRepository.findAll().stream().map(BookMapper::mapToBookDto)
                    .filter(book -> author == null || book.getAuthor().equals(author))
                    .filter(book -> priceBiggerThan == null || book.getPrice() > priceBiggerThan)
                    .filter(book -> priceLessThan == null || book.getPrice() < priceLessThan)
                    .filter(book -> yearBiggerThan == null || book.getYear() > yearBiggerThan)
                    .filter(book -> yearLessThan == null || book.getYear() < yearLessThan)
                    .filter(book -> genresList == null || book.getGenres().containsAll(genresList))
                    .toList();
        }
        else if (persistenceMethod.equals(MONGO_DB)) {
            return mongoBookRepository.findAll().stream().map(book -> BookMapper.mapToBookDto(book))
                    .filter(book -> author == null || book.getAuthor().equals(author))
                    .filter(book -> priceBiggerThan == null || book.getPrice() > priceBiggerThan)
                    .filter(book -> priceLessThan == null || book.getPrice() < priceLessThan)
                    .filter(book -> yearBiggerThan == null || book.getYear() > yearBiggerThan)
                    .filter(book -> yearLessThan == null || book.getYear() < yearLessThan)
                    .filter(book -> genresList == null || book.getGenres().containsAll(genresList))
                    .toList();
        }
        else {
            throw new IllegalArgumentException("Error: Persistence Method " + persistenceMethod + " not found");
        }
    }

    @Override
    public BookDto findById(Integer id, String persistenceMethod) {
        if (persistenceMethod.equals(MONGO_DB)) {
            return mongoBookRepository.findByRawid(id)
                    .map(BookMapper::mapToBookDto)
                    .orElseThrow(() -> new IllegalArgumentException("Error: Book with ID " + id + " not found"));
        } else if (persistenceMethod.equals(POSTGRES_DB)) {
            return postgresBookRepository.findById(id)
                    .map(BookMapper::mapToBookDto)
                    .orElseThrow(() -> new IllegalArgumentException("Error: Book with ID " + id + " not found"));
        } else {
            throw new IllegalArgumentException("Error: Persistence Method " + persistenceMethod + " not found");
        }
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        if (mongoBookRepository.existsByTitle(bookDto.getTitle())) {
            throw new IllegalArgumentException("Error: Book with Title " + bookDto.getTitle() + " already exists");
        }
        if(postgresBookRepository.existsByTitle(bookDto.getTitle())) {
            throw new IllegalArgumentException("Error: Book with Title " + bookDto.getTitle() + " already exists");
        }
        bookDto.setId(findAll().size()+1);
        Book book = BookMapper.mapToBook(bookDto);
        mongoBookRepository.save(book);
        return BookMapper.mapToBookDto(postgresBookRepository.save(book));
    }

    @Override
    public BookDto updatePrice(Integer id,Integer price) {
        Book bookToUpdatePostgres = postgresBookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Error: Book with ID " + id + " not found"));
        Book bookToUpdateMongo = mongoBookRepository.findByRawid(id).orElseThrow(() -> new IllegalArgumentException("Error: Book with ID " + id + " not found"));
        BookDto bookBeforeUpdate = BookMapper.mapToBookDto(bookToUpdatePostgres);
        bookToUpdatePostgres.setPrice(price);
        bookToUpdateMongo.setPrice(price);
        postgresBookRepository.save(bookToUpdatePostgres);
        mongoBookRepository.deleteByRawid(id);
        mongoBookRepository.save(bookToUpdateMongo);
        return bookBeforeUpdate;
    }

    @Override
    public void deleteBook(Integer id) {
        if(!postgresBookRepository.existsById(id)) {
            throw new IllegalArgumentException("Error: Book with ID " + id + " not found");
        }
        postgresBookRepository.deleteById(id);
        mongoBookRepository.deleteByRawid(id);
    }
}
