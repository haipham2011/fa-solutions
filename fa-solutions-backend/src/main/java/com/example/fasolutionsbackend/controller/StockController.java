package com.example.fasolutionsbackend.controller;

import com.example.fasolutionsbackend.dto.Company;
import com.example.fasolutionsbackend.dto.Statistics;
import com.example.fasolutionsbackend.service.StockServiceImpl;
import com.example.fasolutionsbackend.utils.DateRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "stock")
public class StockController {
    private static final String DEFAULT_DATE_RANGE = DateRange.Constants.THREE_VALUE;

    @Autowired
    StockServiceImpl stockServiceImpl;

    @GetMapping("")
    public ResponseEntity<List<Company>> getOverview() {
        List<Company> companies = stockServiceImpl.getStockOverview();
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    @GetMapping("/{companyName}")
    public ResponseEntity<?> getCompanyByName(@PathVariable String companyName,
                                           @RequestParam(name="startDate", required=false) String startDate,
                                           @RequestParam(name="endDate", required=false) String endDate,
                                           @RequestParam(name="dateRange", required=false)
                                        String dateRange) {
        Company company = stockServiceImpl.getCompanyByName(companyName, startDate, endDate, DateRange.fromString(dateRange));
        if (company == null) {
            return new ResponseEntity<>("Cannot find company", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @GetMapping("/statistics/{companyName}")
    public ResponseEntity<?> getAnalytics(@PathVariable String companyName,
                                       @RequestParam(name="startDate", required=false) String startDate,
                                       @RequestParam(name="endDate", required=false) String endDate,
                                       @RequestParam(name="dateRange", required=false)
                                            String dateRange) {
        Statistics statistics = stockServiceImpl.getStockStatistics(companyName, startDate, endDate, DateRange.fromString(dateRange));
        if (statistics == null) {
            return new ResponseEntity<>("Cannot find statistics about the company", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }
}
