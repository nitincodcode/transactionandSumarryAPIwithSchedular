package com.nitin.transactionandproduct.rest.service;

import com.nitin.transactionandproduct.rest.model.Result;
import com.nitin.transactionandproduct.rest.model.SummaryByCityName;
import com.nitin.transactionandproduct.rest.model.Transaction;
import com.nitin.transactionandproduct.scheduler.dao.ProductDAO;
import com.nitin.transactionandproduct.scheduler.dao.TransactionDAO;
import com.nitin.transactionandproduct.util.ApplicationUtils;
import com.nitin.transactionandproduct.rest.model.SummaryByProducts;
import com.nitin.transactionandproduct.scheduler.TransactionScheduler;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final int MAX_THRESHOLD_DAYS_FOR_GET_SUMMARY = 10;

    @Override
    public Transaction retrieveTransactionById(Long id) {
        TransactionDAO transactionDAO = TransactionScheduler.getTransactionMap().get(id);
        if(transactionDAO==null){
            return null;
        }
        ProductDAO productDAO = TransactionScheduler.getProductMap().get(transactionDAO.getProductId());
        String productName = productDAO!=null?productDAO.getProductName():"";
        return new Transaction(transactionDAO.getTransactionId(),
                productName,
                transactionDAO.getTransactionAmount(),
                transactionDAO.getTransactionDatetime().format(ApplicationUtils.FORMATTER));
    }

    @Override
    public Result retrieveSummaryTransactionByProductAndDays(Integer days) {
        if(days> MAX_THRESHOLD_DAYS_FOR_GET_SUMMARY) days= MAX_THRESHOLD_DAYS_FOR_GET_SUMMARY;
        LocalDate localDate = LocalDate.now().minusDays(days);
        Map<String, ProductDAO> productDAOMap = TransactionScheduler.getProductMap();
        Map<String, List<TransactionDAO>> transactionGroupByProduct =
                TransactionScheduler.getTransactionGroupByProduct();
        Set<String> productSet = transactionGroupByProduct.keySet();
        List<SummaryByProducts> summaryList = new ArrayList<>();
        for(String prdId: productSet){
            ProductDAO productDAO = productDAOMap.get(prdId);
            if(productDAO!=null) {
                Double totalCount = transactionGroupByProduct
                        .get(prdId)
                        .stream()
                        .filter(transactionDAO ->
                                transactionDAO.getTransactionDatetime().toLocalDate().isAfter(localDate)
                                ||transactionDAO.getTransactionDatetime().toLocalDate().isEqual(localDate))
                        .mapToDouble(TransactionDAO::getTransactionAmount)
                        .sum();
                summaryList.add(new SummaryByProducts(productDAO.getProductName(),totalCount));
            }
        }
        return new Result(summaryList);
    }

    @Override
    public Result retrieveSummaryTransactionByCityNameAndDays(Integer days) {
        if(days> MAX_THRESHOLD_DAYS_FOR_GET_SUMMARY) days= MAX_THRESHOLD_DAYS_FOR_GET_SUMMARY;
        LocalDate localDate = LocalDate.now().minusDays(days);
        Map<String, List<TransactionDAO>> transactionGroupByManufacturingCity =
                TransactionScheduler.getTransactionGroupByManufacturingCity();
        Set<String> citySet = transactionGroupByManufacturingCity.keySet();
        List<SummaryByCityName> summaryList = new ArrayList<>();
        for(String city: citySet){
            if(!city.equals("")) {
                Double totalCount = transactionGroupByManufacturingCity.get(city)
                        .stream()
                        .filter(transactionDAO ->
                                transactionDAO.getTransactionDatetime().toLocalDate().isAfter(localDate)
                                ||transactionDAO.getTransactionDatetime().toLocalDate().isEqual(localDate))
                        .mapToDouble(TransactionDAO::getTransactionAmount)
                        .sum();
                summaryList.add(new SummaryByCityName(city,totalCount));
            }
        }
        return new Result(summaryList);
    }


}
