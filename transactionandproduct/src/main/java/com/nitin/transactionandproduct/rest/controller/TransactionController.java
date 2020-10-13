package com.nitin.transactionandproduct.rest.controller;

import com.nitin.transactionandproduct.rest.service.TransactionService;
import com.nitin.transactionandproduct.rest.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assignment")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity retrieveTransactionByID(@PathVariable Long transactionId) {
        Transaction transaction = transactionService.retrieveTransactionById(transactionId);
        if(transaction!=null)
            return ResponseEntity.ok().body(transactionService.retrieveTransactionById(transactionId));
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("/transactionSummaryByProducts/{days}")
    public ResponseEntity retrieveTransactionSummaryByProducts(@PathVariable Integer days){
        return ResponseEntity.ok().body(transactionService.retrieveSummaryTransactionByProductAndDays(days));
    }

    @GetMapping("/transactionSummaryByManufacturingCity/{days}")
    public ResponseEntity retrieveTransactionSummaryByManufacturingCity(@PathVariable Integer days){
        return ResponseEntity.ok().body(transactionService.retrieveSummaryTransactionByCityNameAndDays(days));
    }


}
