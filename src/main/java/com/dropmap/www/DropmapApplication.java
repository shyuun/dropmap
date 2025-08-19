package com.dropmap.www;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DropmapApplication {

    public static void main(String[] args) {
        SpringApplication.run(DropmapApplication.class, args);
    }

}
