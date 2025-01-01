package com.example.demo.model;

import jakarta.persistence.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import org.springframework.data.annotation.Id;

@Entity
@Table(name = "books")
@Document(collection = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="rawid", nullable = false,unique = true)
    private Long id;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="author", nullable = false)
    private String author;

    @Column(name="year", nullable = false)
    private int year;

    @Column(name="price", nullable = false)
    private int price;

    @ElementCollection
    @Column(name="genres", nullable = false)
    private List<String> genres;

    public Book(String title, String author, int year, int price, List<String> genres) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.price = price;
        this.genres = genres;
    }

    public Book() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
}
