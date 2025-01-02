package com.example.demo.service;

import com.example.demo.dto.BookDto;

import java.util.List;

public interface BookService {

    List<BookDto> findAll();

    List<BookDto> findByFilter(String author,Integer priceBiggerThan,Integer priceLessThan, Integer yearBiggerThan,Integer yearLessThan, String genres, String persistenceMethod);

    BookDto findById(Integer id,String persistenceMethod);

    BookDto createBook(BookDto bookDto);

    BookDto updatePrice(Integer id,Integer price);

    void deleteBook(Integer id);
}
