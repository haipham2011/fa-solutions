package com.example.fasolutionsbackend.utils;

public enum DateRange {
    THREE(Constants.THREE_VALUE),
    WEEK(Constants.WEEK_VALUE),
    MONTH(Constants.MONTH_VALUE),
    HALF_YEAR(Constants.HALF_YEAR_VALUE),
    YEAR(Constants.YEAR_VALUE);

    private final String range;

    DateRange(final String range) {
        this.range = range;
    }

    public String getValue() {
        return range;
    }

    public static DateRange fromString(final String range) {
        for (DateRange dateRange : DateRange.values()) {
            if (dateRange.range.equalsIgnoreCase(range)) {
                return dateRange;
            }
        }
        return null;
    }

    public static class Constants {
        public static final String THREE_VALUE = "3d";
        public static final String WEEK_VALUE = "w";
        public static final String MONTH_VALUE = "m";
        public static final String HALF_YEAR_VALUE = "6m";
        public static final String YEAR_VALUE = "y";
    }
}
