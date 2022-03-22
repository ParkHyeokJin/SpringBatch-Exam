package com.example.springbatchexam;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class SpringBatchExamApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchExamApplication.class, args);
    }

}
