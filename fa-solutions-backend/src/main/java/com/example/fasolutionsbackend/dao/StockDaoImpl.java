package com.example.fasolutionsbackend.dao;

import com.example.fasolutionsbackend.dto.Company;
import com.example.fasolutionsbackend.dto.Stock;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StockDaoImpl implements StockDao {

    private static List<Company> readCompanies() {
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

    public List<Company> getAllCompanies() {
        return readCompanies();
    }

    public Company findCompany(String name) {
        List<Company> companies = readCompanies();
        if (companies == null) {
            return null;
        }
        return companies.stream().filter(company -> company.getName().equals(name))
                .findAny().orElse(null);
    }
}
