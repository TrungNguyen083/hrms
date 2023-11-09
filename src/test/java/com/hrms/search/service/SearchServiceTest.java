package com.hrms.search.service;

import com.hrms.search.document.Employee;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class SearchServiceTest {

    @Autowired
    private SearchService searchService;

    @Test
    void testAddEmployee() {
        var emp1 = Employee.builder()
                .id("1")
                .firstName("John")
                .lastName("Doe")
                .build();

        var emp2 = Employee.builder()
                .id("2")
                .firstName("Jane")
                .lastName("Doe")
                .build();

        var emp3 = Employee.builder()
                .id("3")
                .firstName("John")
                .lastName("Smith")
                .build();

        searchService.createEmployeeIndexBulk(List.of(emp1, emp2, emp3));
        for (var e : searchService.searchEmployee("Deo")) {
            log.info("Employee: {}", e.getFirstName() + " " + e.getLastName());
        }
    }

}