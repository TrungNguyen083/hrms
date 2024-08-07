//package com.hrms.performancemanegment;
//
//import com.hrms.performancemanagement.services.impl.PerformanceServiceImpl;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//
//@SpringJUnitConfig
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class PerformanceServiceTest {
//    @Autowired
//    private PerformanceServiceImpl performanceService;
//
//    @Test
//    void getAllPerformanceCycles_shouldReturnAllPerformanceCycles() {
//        performanceService.getAllPerformanceCycles();
//        Assertions.assertEquals(7, performanceService.getAllPerformanceCycles().size());
//    }
//
//    @Test
//    void getPerformanceEvaluations_shouldReturnAllPerformanceEvaluations() {
//        var r = performanceService.getPerformanceEvaluations(4, PageRequest.of(0, 10));
//        Assertions.assertNotNull(r);
//    }
//
//    @Test
//    void getPerformanceEvaluations_employeeDoesNotHaveEvaluation_shouldReturnEmptyList() {
//        var r = performanceService.getPerformanceEvaluations(7, PageRequest.of(0, 10));
//        Assertions.assertTrue(r.isEmpty());
//    }
//
//    @Test
//    void getPerformanceByJobLevel_shouldReturnPerformanceByJobLevel() {
//        var r = performanceService.getPerformanceByJobLevel(1, 3);
//        Assertions.assertNotNull(r);
//        Assertions.assertFalse(r.getDatasets().isEmpty());
//    }
//
//
//}
