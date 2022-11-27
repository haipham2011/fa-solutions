package com.example.fasolutionsbackend.dto;

import java.util.ArrayList;
import java.util.List;

public class Company {
    private final String name;
    private List<Stock> stockList = new ArrayList<>();

    public Company(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock> stocks) {
        this.stockList = stocks;
    }

    public void addStock(Stock stock) {
        this.stockList.add(stock);
    }
}
