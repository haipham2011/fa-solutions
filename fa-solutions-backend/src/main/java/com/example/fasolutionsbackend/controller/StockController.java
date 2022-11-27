package com.example.fasolutionsbackend.controller;

import com.example.fasolutionsbackend.dto.Company;
import com.example.fasolutionsbackend.dto.Statistics;
import com.example.fasolutionsbackend.dto.Stock;
import com.example.fasolutionsbackend.service.StockService;
import com.example.fasolutionsbackend.utils.DateRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "stock")
public class StockController {
    private static final String DEFAULT_DATE_RANGE = DateRange.Constants.THREE_VALUE;

    @Autowired
    StockService stockService;

    @GetMapping("")
    public List<Company> getOverview() {
        return stockService.getStockOverview();
    }

    @GetMapping("/{companyName}")
    public Company getCompanyByName(@PathVariable String companyName,
                                @RequestParam(name="startDate", required=false) String startDate,
                                @RequestParam(name="dateRange", required=false, defaultValue=DEFAULT_DATE_RANGE)
                                        String dateRange) {
        return stockService.getCompanyByName(companyName, startDate, DateRange.fromString(dateRange));
    }

    @GetMapping("/statistics/{companyName}")
    public Statistics getAnalytics(@PathVariable String companyName,
                                   @RequestParam(name="startDate", required=false) String startDate,
                                   @RequestParam(name="dateRange", required=false, defaultValue=DEFAULT_DATE_RANGE)
                                            String dateRange) {
        return stockService.getStockStatistics(companyName, startDate, DateRange.fromString(dateRange));
    }
}
