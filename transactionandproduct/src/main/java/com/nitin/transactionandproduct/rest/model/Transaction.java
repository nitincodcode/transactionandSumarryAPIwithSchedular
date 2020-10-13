package com.nitin.transactionandproduct.rest.model;

public class Transaction {
    private Long transactionId;
    private String productName;
    private Double transactionAmount;
    private String transactionDatetime;

    public Transaction(Long transactionId, String productName, Double transactionAmount, String transactionDatetime) {
        this.transactionId = transactionId;
        this.productName = productName;
        this.transactionAmount = transactionAmount;
        this.transactionDatetime = transactionDatetime;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public String getProductName() {
        return productName;
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public String getTransactionDatetime() {
        return transactionDatetime;
    }
}
