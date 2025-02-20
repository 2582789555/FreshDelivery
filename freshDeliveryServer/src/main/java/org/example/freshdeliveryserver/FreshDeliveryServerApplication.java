package org.example.freshdeliveryserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan("org.example.freshdeliveryserver.mappers")
@EnableAsync
public class FreshDeliveryServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreshDeliveryServerApplication.class, args);
    }

}
