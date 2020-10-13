package com.nitin.transactionandproduct.rest.service;

import com.nitin.transactionandproduct.rest.model.Result;
import com.nitin.transactionandproduct.rest.model.Transaction;

public interface TransactionService {
    Transaction retrieveTransactionById(Long id);
    Result retrieveSummaryTransactionByProductAndDays(Integer days);
    Result retrieveSummaryTransactionByCityNameAndDays(Integer days);
}
