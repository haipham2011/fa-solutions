package com.example.fasolutionsbackend.service;

import com.example.fasolutionsbackend.dto.Company;
import com.example.fasolutionsbackend.dto.Statistics;
import com.example.fasolutionsbackend.dto.Stock;
import com.example.fasolutionsbackend.utils.Common;
import com.example.fasolutionsbackend.utils.DateRange;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
public class StockService {

    private static List<Company> getCompanies() {
        String line = "";
        String splitBy = ",";
        int rowIndex = 0;
        List<Company> companies = null;
        try {
            //parsing a CSV file into BufferedReader class constructor
            InputStream resource = new ClassPathResource("data/DATA.csv").getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(resource));
            while ((line = br.readLine()) != null)   //returns a Boolean value
            {
                if (rowIndex == 0) {
                    companies = Arrays.stream(line.split(splitBy)).skip(1).map(Company::new).collect(Collectors.toList());
                } else {
                    String[] elements = line.split(splitBy);
                    for (int i = 0; i < elements.length - 1; i++) {
                        companies.get(i).addStock(new Stock(Double.parseDouble(elements[i + 1]),elements[0]));
                    }
                }
                rowIndex += 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return companies;
    }

    private static int getDayOffset(final DateRange dateRange) {
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

    private static Company findCompany(String name) {
        List<Company> companies = getCompanies();

        return companies.stream().filter(company -> company.getName().equals(name))
                .findAny().orElse(null);
    }

    public List<Company> getStockOverview() {
        List<Company> companies = getCompanies();
        return companies.stream().map(company -> {
            Company newCompany = new Company(company.getName());
            newCompany.setStockList(Common.getSliceOfStream(company.getStockList().stream(),
                    company.getStockList().size() - 3,
                    company.getStockList().size() - 1).collect(Collectors.toList()));
            return newCompany;
        }).collect(Collectors.toList());
    }

    private List<Stock> getStockList(final String name, final String startDate, final DateRange dateRange, final Company company) {
        Company foundCompany = company == null ? findCompany(name) : company;
        String foundDate = startDate == null ? foundCompany.getStockList().get(0).getDate() : startDate;
        int index = IntStream.range(0, foundCompany.getStockList().size())
                .filter(i -> foundDate.equals(foundCompany.getStockList().get(i).getDate()))
                .findFirst().orElse(0);

        int dayOffset = getDayOffset(dateRange);
        List<Stock> stocks = Common.getSliceOfStream(foundCompany.getStockList().stream(), index, index + dayOffset -1)
                .collect(Collectors.toList());

        return stocks;
    }

    public Company getCompanyByName(final String name, final String startDate, final DateRange dateRange) {
        Company foundCompany = findCompany(name);
        List<Stock> stockList = getStockList(name, startDate, dateRange, foundCompany);
        foundCompany.setStockList(stockList);

        return foundCompany;
    }

    public Statistics getStockStatistics(final String name, final String startDate, final DateRange dateRange) {
        List<Stock> stockList = getStockList(name, startDate, dateRange, null);
        Stock minStock = stockList.stream().min(Comparator.comparing(Stock::getPrice)).orElse(null);
        Stock maxStock = stockList.stream().max(Comparator.comparing(Stock::getPrice)).orElse(null);
        Double changeRate = calculateChangeRate(stockList.get(0).getPrice(), stockList.get(stockList.size() - 1).getPrice());
        return new Statistics(minStock, maxStock, changeRate);
    }
}
