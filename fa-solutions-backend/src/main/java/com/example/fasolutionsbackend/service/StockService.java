package com.example.fasolutionsbackend.service;

import com.example.fasolutionsbackend.dto.Company;
import com.example.fasolutionsbackend.dto.Statistics;
import com.example.fasolutionsbackend.utils.DateRange;

import java.util.List;

public interface StockService {
    /**
     Get all companies with stock in 3 days
     */
    List<Company> getStockOverview();

    /**
     Get company stock by name and other parameters: start date, end date or date range
     */
    Company getCompanyByName(final String name, final String startDate, final String endDate, final DateRange dateRange);

    /**
     Get company stock statistics by name and other parameters: start date, end date or date range
     */
    Statistics getStockStatistics(final String name, final String startDate, final String endDate, final DateRange dateRange);
}
