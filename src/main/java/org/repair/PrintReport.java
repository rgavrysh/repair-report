package org.repair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.concurrent.ForkJoinPool;


@EnableJpaRepositories
@SpringBootApplication
public class PrintReport {

    public static void main(String[] args) {
        SpringApplication.run(PrintReport.class, args);
    }
}