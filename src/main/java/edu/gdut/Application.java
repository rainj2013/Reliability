package edu.gdut;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-3
 */
@SpringBootApplication
@MapperScan("edu.gdut.dao")
public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }
}
