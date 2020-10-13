package com.nitin.transactionandproduct.scheduler.dao;

import com.nitin.transactionandproduct.scheduler.TransactionScheduler;

import java.time.LocalDateTime;

public class TransactionDAO {

    private Long transactionId;
    private String productId;
    private String cityName;
    private Double transactionAmount;
    private LocalDateTime transactionDatetime;

    public TransactionDAO(Long transactionId, String productId,
                          Double transactionAmount,
                          LocalDateTime transactionDatetime) {
        this.transactionId = transactionId;
        this.productId = productId.trim();
        this.transactionAmount = transactionAmount;
        this.transactionDatetime = transactionDatetime;
        this.cityName = TransactionScheduler.getProductMap().get(this.productId)!=null
                ? TransactionScheduler.getProductMap().get(this.productId).getProductManufacturingCity()
                :"";
    }

    public String getCityName() {
        return cityName;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public String getProductId() {
        return productId;
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public LocalDateTime getTransactionDatetime() {
        return transactionDatetime;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TransactionDAO)) {
            return false;
        }
        TransactionDAO transaction = (TransactionDAO) obj;
        return transactionId.equals(transaction.getTransactionId());
    }
}
