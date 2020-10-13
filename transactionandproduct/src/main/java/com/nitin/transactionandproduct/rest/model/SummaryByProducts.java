package com.nitin.transactionandproduct.rest.model;

public class SummaryByProducts {
    private String productName;
    private Double totalCount;

    public SummaryByProducts(String productName, Double totalCount) {
        this.productName = productName;
        this.totalCount = totalCount;
    }

    public String getProductName() {
        return productName;
    }

    public Double getTotalCount() {
        return totalCount;
    }
}
