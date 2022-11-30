package com.example.fasolutionsbackend.dao;

import com.example.fasolutionsbackend.dto.Company;

import java.util.List;

public interface StockDao {
    /**
     Get all companies from CSV file
     */
    List<Company> getAllCompanies();

    /**
     Find company with name
     */
    Company findCompany(String name);
}
