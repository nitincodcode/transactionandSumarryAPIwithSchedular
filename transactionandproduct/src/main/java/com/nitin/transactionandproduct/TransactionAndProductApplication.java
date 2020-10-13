package com.nitin.transactionandproduct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TransactionAndProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionAndProductApplication.class, args);
	}

}
