package com.example.fasolutionsbackend.dto;


public class Stock {
    private final Double price;

    private final String date;

    public Stock(final Double price, final String date) {
        this.price = price;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public Double getPrice() {
        return price;
    }
}
