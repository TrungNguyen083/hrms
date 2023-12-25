package com.hrms.performancemanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PerformanceServiceTest {

    private PerformanceService performanceService;

    @Container
    static GenericContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql:latest"))
            .withExposedPorts(3306)
            .withDatabaseName("hrms")
            .withUsername("root")
            .withPassword("root");


    @BeforeEach
    public void init() {

    }

    @Test
    void getAllPerformanceCycles() {
    }

    @Test
    void getPerformanceEvaluations() {
    }

    @Test
    void getAveragePerformanceScore() {
    }

    @Test
    void getPerformanceRatingScheme() {
    }

    @Test
    void getPerformanceByJobLevel() {
    }

    @Test
    void getPerformanceOverview() {
    }

    @Test
    void getPotentialAndPerformance() {
    }

    @Test
    void getEmployeePerformanceRatingScore() {
    }
}