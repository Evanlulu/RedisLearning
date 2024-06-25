package com.evan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan("com.evan.mapper")
@SpringBootApplication
public class EvanApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(EvanApplication.class, args);
    }

}
