package com.kshrd.habittracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableCaching
//@MapperScan("com.kshrd.habittracker.mapper")
public class HabittrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HabittrackerApplication.class, args);
    }

}
