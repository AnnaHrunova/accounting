package com.mintos.accounting;

import org.springframework.boot.SpringApplication;

public class LocalAccountingApplication {
    public static void main(String[] args) {
        SpringApplication.from(AccountingApplication::main)
                .with(LocalDevTestcontainersConfig.class)
                .run(args);
    }
}
