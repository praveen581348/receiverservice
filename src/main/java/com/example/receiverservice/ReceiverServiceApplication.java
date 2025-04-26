package com.example.receiverservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.example.receiverservice.entity") // Specify the package containing your entities
public class ReceiverServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReceiverServiceApplication.class, args);
    }

}