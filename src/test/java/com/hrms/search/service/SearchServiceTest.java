package com.hrms.search.service;

import com.hrms.search.document.EmployeeDocument;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class SearchServiceTest {

    @Autowired
    private SearchService searchService;

    @Test
    void testAddEmployee() {
        var emp1 = EmployeeDocument.builder()
                .id("1")
                .firstName("John")
                .lastName("Doe")
                .build();

        var emp2 = EmployeeDocument.builder()
                .id("2")
                .firstName("Jane")
                .lastName("Doe")
                .build();

        var emp3 = EmployeeDocument.builder()
                .id("3")
                .firstName("John")
                .lastName("Smith")
                .build();

        for (var e : searchService.searchEmployee("Deo")) {
            log.info("EmployeeDocument: {}", e.getFirstName() + " " + e.getLastName());
        }
    }

}