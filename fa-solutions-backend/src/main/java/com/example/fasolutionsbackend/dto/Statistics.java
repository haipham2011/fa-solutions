package com.example.fasolutionsbackend.dto;

public class Statistics {
    private final Stock min;
    private final Stock max;
    private final Double changeRate;

    public Statistics(final Stock min, final Stock max, final Double changeRate) {
        this.min = min;
        this.max = max;
        this.changeRate = changeRate;
    }

    public Stock getMin() {
        return min;
    }

    public Stock getMax() {
        return max;
    }

    public Double getChangeRate() {
        return changeRate;
    }
}
