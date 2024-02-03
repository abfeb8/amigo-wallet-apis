package com.ab.amigo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CustomerMSApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerMSApplication.class, args);
    }
}
