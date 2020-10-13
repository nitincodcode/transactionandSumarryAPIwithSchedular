package com.nitin.transactionandproduct.scheduler.service;

import com.nitin.transactionandproduct.scheduler.dao.TransactionDAO;
import com.nitin.transactionandproduct.scheduler.TransactionScheduler;
import com.nitin.transactionandproduct.util.ApplicationUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class FileServiceImpl implements FileService {

    private static Set<String> processedFileSet = new HashSet<>();

    @Override
    public void addProcessedFile(File file) {
        processedFileSet.add(file.getName());
    }

    @Override
    public Boolean isFileAlreadyProcessed(File file) {
        return processedFileSet.contains(file.getName());

    }

    @Override
    public void processTransactionFile(File file) {
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            bufferedReader.readLine();
            String line = bufferedReader.readLine();
            while (line != null) {
                try{
                    String[] transactionDetails = line.split(",");
                    TransactionScheduler.getTransactionList().add(new TransactionDAO(
                            Long.parseLong(transactionDetails[0]),
                            transactionDetails[1],
                            Double.parseDouble(transactionDetails[2]),
                            LocalDateTime.parse(transactionDetails[3].trim(),ApplicationUtils.FORMATTER))
                        );
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    line = bufferedReader.readLine();
                }

            }
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

}
