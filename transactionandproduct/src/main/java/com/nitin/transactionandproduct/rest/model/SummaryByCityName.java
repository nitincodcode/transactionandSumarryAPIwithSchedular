package com.nitin.transactionandproduct.rest.model;

public class SummaryByCityName {
        private String cityName;
        private Double totalCount;

    public SummaryByCityName(String cityName, Double totalCount) {
        this.cityName = cityName;
        this.totalCount = totalCount;
    }

    public String getCityName() {
        return cityName;
    }

    public Double getTotalCount() {
        return totalCount;
    }
}
