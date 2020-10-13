package com.nitin.transactionandproduct.scheduler;


import com.nitin.transactionandproduct.scheduler.dao.ProductDAO;
import com.nitin.transactionandproduct.scheduler.dao.TransactionDAO;
import com.nitin.transactionandproduct.scheduler.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TransactionScheduler {

    private final int DELAY_POLL_FILES = 1000;
    private final int DELAY_SUMMARY_BY_PRODUCT = 1500;
    private final int DELAY_SUMMARY_BY_CITY = 1500;
    private final int INTERVAL_POLL_FILES = 1000;
    private final int INTERVAL_SUMMARY_BY_PRODUCT = 1500;
    private final int INTERVAL_SUMMARY_BY_CITY = 1500;

    private static final String PRODUCT_FILE_PATH = System.getProperty("productFolder");
    private static final String PRODUCT_FILE_NAME = System.getProperty("productFile");
    private final String TRANSACTION_FILE_PATH = System.getProperty("transactionFolder");

    private static Map<String, ProductDAO> productMap;
    private static List<TransactionDAO> transactionList = new ArrayList<>();
    private static Map<Long, TransactionDAO> transactionMap = new HashMap<>();
    private static Map<String, List<TransactionDAO>> transactionGroupByManufacturingCity = new HashMap<>();
    private static Map<String, List<TransactionDAO>> transactionGroupByProduct = new HashMap<>();

    private AtomicBoolean changeSummaryByProduct = new AtomicBoolean(false);
    private AtomicBoolean changeSummaryByCity = new AtomicBoolean(false);

    @Autowired
    private FileService fileService;

    static {
        try {
            productMap = loadProductMaster();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    //getters
    public static Map<String, ProductDAO> getProductMap() {
        return productMap;
    }
    public static Map<Long, TransactionDAO> getTransactionMap() {
        return transactionMap;
    }
    public static Map<String, List<TransactionDAO>> getTransactionGroupByProduct() {
        return transactionGroupByProduct;
    }
    public static Map<String, List<TransactionDAO>> getTransactionGroupByManufacturingCity() {
        return transactionGroupByManufacturingCity;
    }
    public static List<TransactionDAO> getTransactionList() {
        return transactionList;
    }

    private static Map<String, ProductDAO> loadProductMaster() throws IOException {
        Path path = Paths.get(PRODUCT_FILE_PATH, PRODUCT_FILE_NAME);
        File productList = new File(path.toAbsolutePath().toString());
        if(!productList.isFile()){
            throw new FileNotFoundException();
        }
        List<ProductDAO> bufferProductList = processProductFile(productList);
        return bufferProductList.stream().collect(Collectors.toMap(ProductDAO::getProductId, Function.identity()));
    }

    private static List<ProductDAO> processProductFile(File file) throws IOException {
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            final int[] count = {0};
            return bufferedReader.lines().skip(1).map(line -> {
                try{
                    String[] productDetails = line.split(",");
                    return new ProductDAO(productDetails[0], productDetails[1], productDetails[2]);
                }
                catch(Exception e){
                    e.printStackTrace();
                    return null;
                }
            }).collect(Collectors.toList());
        }
    }

    //scheduler
    @Scheduled(fixedRate = INTERVAL_POLL_FILES, initialDelay = DELAY_POLL_FILES)
    public void pollFiles() {
        Path path = Paths.get(TRANSACTION_FILE_PATH);
        File[] fileList = new File(path.toAbsolutePath().toString())
                .listFiles(filename -> filename.getName().endsWith(".csv"));
        if (fileList != null) {
            for (File file : fileList) {
                if (!fileService.isFileAlreadyProcessed(file)) {
                    fileService.processTransactionFile(file);
                    transactionMap = transactionList
                            .stream()
                            .collect(Collectors.toMap(TransactionDAO::getTransactionId, Function.identity()));
                    fileService.addProcessedFile(file);
                    changeSummaryByProduct.set(true);
                    changeSummaryByCity.set(true);
                }
            }
        }
    }

    @Scheduled(fixedRate = INTERVAL_SUMMARY_BY_PRODUCT, initialDelay = DELAY_SUMMARY_BY_PRODUCT)
    public void updateSummaryByProduct(){
        if(changeSummaryByProduct.get()) {
            transactionGroupByProduct = transactionList
                    .stream()
                    .collect(Collectors.groupingBy(TransactionDAO::getProductId));
            changeSummaryByProduct.set(false);
        }
    }

    @Scheduled(fixedRate = INTERVAL_SUMMARY_BY_CITY, initialDelay = DELAY_SUMMARY_BY_CITY)
    public void updateSummaryByCityName(){
        if(changeSummaryByCity.get()) {
            transactionGroupByManufacturingCity = transactionList
                    .stream()
                    .collect(Collectors.groupingBy(TransactionDAO::getCityName));
            changeSummaryByCity.set(false);
        }
    }

}
