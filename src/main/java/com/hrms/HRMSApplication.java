package com.hrms;

import com.hrms.search.document.Employee;
import com.hrms.search.repository.EmpRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

@ComponentScans(
        @ComponentScan({"com.hrms.employeemanagement.*",
                "com.hrms.usermanagement.*",
                "com.hrms.careerpathmanagement.*",
                "com.hrms.digitalassetmanagement.*",
                "com.hrms.performancemanagement.*",
                "com.hrms.global.*",
                "com.hrms.search.*",
        }))
@EntityScan({"com.hrms.usermanagement.model",
        "com.hrms.employeemanagement.models",
        "com.hrms.competencymanagement.models",
        "com.hrms.careerpathmanagement.models",
        "com.hrms.damservice.models",
        "com.hrms.performancemanagement.model"
})
@SpringBootApplication
@CrossOrigin
public class HRMSApplication {
    @Autowired
    EmpRepository employeeRepository;

    public static void main(String[] args) {
        SpringApplication.run(HRMSApplication.class, args);
    }
}
