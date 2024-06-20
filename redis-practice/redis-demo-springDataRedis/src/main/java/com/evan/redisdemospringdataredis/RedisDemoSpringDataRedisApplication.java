package com.evan.redisdemospringdataredis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class RedisDemoSpringDataRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisDemoSpringDataRedisApplication.class, args);
    }

}
