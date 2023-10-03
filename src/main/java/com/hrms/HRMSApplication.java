package com.hrms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScans(
        @ComponentScan({"com.hrms.usermanagement.*"
                , "com.hrms.employeemanagement.*"
        }))
@EntityScan({"com.hrms.usermanagement.model","com.hrms.employeemanagement.models"})
@EnableJpaRepositories({"com.hrms.usermanagement.repository","com.hrms.employeemanagement.repositories"})
@SpringBootApplication
public class HRMSApplication {
    public static void main(String[] args) {
        SpringApplication.run(HRMSApplication.class, args);
    }
}