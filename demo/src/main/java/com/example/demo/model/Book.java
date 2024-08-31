package com.example.demo.model;

import java.util.List;

public class Book {
    private Long id;
    private String title;
    private String author;
    private int year;
    private int price;
    private List<String> genres;

    public Book(String title, String author, int year, int price, List<String> genres) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.price = price;
        this.genres = genres;
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
