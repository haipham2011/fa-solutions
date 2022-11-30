package com.example.fasolutionsbackend.service;

import com.example.fasolutionsbackend.dao.StockDaoImpl;
import com.example.fasolutionsbackend.dto.Company;
import com.example.fasolutionsbackend.dto.Statistics;
import com.example.fasolutionsbackend.dto.Stock;
import com.example.fasolutionsbackend.utils.Common;
import com.example.fasolutionsbackend.utils.DateRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class StockServiceImpl implements StockService{

    @Autowired
    StockDaoImpl stockDaoImpl;

    private static int getDayOffset(final DateRange dateRange, final int stockSize) {
        if (dateRange == null) {
            return stockSize;
        }
        switch (dateRange) {
            case THREE:
                return 3;
            case WEEK:
                return 7;
            case MONTH:
                return 30;
            case HALF_YEAR:
                return 182;
            case YEAR:
                return 365;
            default:
                return 1;
        }
    }

    private static Double calculateChangeRate(final Double firstVal, final Double secondVal) {
        return (secondVal - firstVal) * 100 / firstVal;
    }

    public List<Company> getStockOverview() {
        List<Company> companies = stockDaoImpl.getAllCompanies();
        if (companies == null || companies.size() == 0) {
            return Collections.emptyList();
        }
        return companies.stream().map(company -> {
            Company newCompany = new Company(company.getName());
            newCompany.setStockList(Common.getSliceOfStream(company.getStockList().stream(),
                    company.getStockList().size() - 3,
                    company.getStockList().size() - 1).collect(Collectors.toList()));
            return newCompany;
        }).collect(Collectors.toList());
    }

    private List<Stock> getStockList(final String name, final String startDate, final String endDate, final DateRange dateRange, final Company company) {
        Company foundCompany = company == null ? stockDaoImpl.findCompany(name) : company;
        if (foundCompany == null) {
            return null;
        }
        List<Stock> stockList = foundCompany.getStockList();
        if (stockList == null || stockList.size() == 0) {
            return Collections.emptyList();
        }
        String foundStartDate = startDate == null ? stockList.get(0).getDate() : startDate;
        String foundEndDate = endDate == null ? stockList.get(stockList.size() - 1).getDate() : endDate;

        if (endDate == null) {
            int index = IntStream.range(0, foundCompany.getStockList().size())
                    .filter(i -> foundStartDate.equals(foundCompany.getStockList().get(i).getDate()))
                    .findFirst().orElse(0);

            int dayOffset = getDayOffset(dateRange, stockList.size());
            List<Stock> stocks = Common.getSliceOfStream(foundCompany.getStockList().stream(), index, index + dayOffset -1)
                    .collect(Collectors.toList());

            return stocks;
        } else {
            int startIndex = IntStream.range(0, foundCompany.getStockList().size())
                    .filter(i -> foundStartDate.equals(foundCompany.getStockList().get(i).getDate()))
                    .findFirst().orElse(0);

            int endIndex = IntStream.range(0, foundCompany.getStockList().size())
                    .filter(i -> foundEndDate.equals(foundCompany.getStockList().get(i).getDate()))
                    .findFirst().orElse(stockList.size() - 1);

            if (startIndex > endIndex) {
                return Collections.emptyList();
            }
            List<Stock> stocks = Common.getSliceOfStream(foundCompany.getStockList().stream(), startIndex, endIndex)
                    .collect(Collectors.toList());

            return stocks;
        }
    }

    public Company getCompanyByName(final String name, final String startDate, final String endDate, final DateRange dateRange) {
        Company foundCompany = stockDaoImpl.findCompany(name);
        if (foundCompany == null) {
            return null;
        }
        List<Stock> stockList = getStockList(name, startDate, endDate, dateRange, foundCompany);
        foundCompany.setStockList(stockList);

        return foundCompany;
    }

    public Statistics getStockStatistics(final String name, final String startDate, final String endDate, final DateRange dateRange) {
        List<Stock> stockList = getStockList(name, startDate, endDate, dateRange, null);
        if (stockList == null || stockList.size() == 0) {
            return null;
        }
        Stock minStock = stockList.stream().min(Comparator.comparing(Stock::getPrice)).orElse(null);
        Stock maxStock = stockList.stream().max(Comparator.comparing(Stock::getPrice)).orElse(null);
        Double changeRate = calculateChangeRate(stockList.get(0).getPrice(), stockList.get(stockList.size() - 1).getPrice());
        return new Statistics(minStock, maxStock, changeRate);
    }
}
