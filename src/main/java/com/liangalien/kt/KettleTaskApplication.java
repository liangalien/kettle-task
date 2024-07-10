package com.liangalien.kt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class KettleTaskApplication {
    public static void main(String[] args) {
        SpringApplication.run(KettleTaskApplication.class, args);
    }

}
