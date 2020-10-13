package com.nitin.transactionandproduct.scheduler.service;

import java.io.File;

public interface FileService {
    Boolean isFileAlreadyProcessed(File file);
    void processTransactionFile(File file);
    void addProcessedFile(File file);
}
