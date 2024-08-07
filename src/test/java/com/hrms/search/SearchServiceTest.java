//package com.hrms.search;
//
//import com.hrms.search.service.SearchService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//
//@SpringJUnitConfig
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class SearchServiceTest {
//    @Autowired
//    SearchService searchService;
//
//    @Test
//    void searchEmployee_trueValue_shouldReturnResult() {
//        var result = searchService.searchEmployee("Peter");
//        Assertions.assertNotNull(result);
//        Assertions.assertFalse(result.isEmpty());
//    }
//
//    @Test
//    void searchEmployee_wrongValue_shouldReturnEmpty() {
//        var result = searchService.searchEmployee("zxcvvbnm");
//        Assertions.assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void searchSkillSet_trueValue_shouldReturnResult() {
//        var result = searchService.searchSkillSet("Java");
//        Assertions.assertNotNull(result);
//        Assertions.assertFalse(result.isEmpty());
//    }
//
//    @Test
//    void searchSkillSet_wrongValue_shouldReturnEmpty() {
//        var result = searchService.searchSkillSet("zxcvvbnm");
//        Assertions.assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void searchCompetency_trueValue_shouldReturnResult() {
//        var result = searchService.searchCompetency("Willingness to");
//        Assertions.assertNotNull(result);
//        Assertions.assertFalse(result.isEmpty());
//        System.out.println(result.get(0).getDisplayText());
//    }
//
//    @Test
//    void searchCompetency_wrongValue_shouldReturnEmpty() {
//        var result = searchService.searchCompetency("zxcvvbnm");
//        Assertions.assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void searchPosition_trueValue_shouldReturnResult() {
//        var result = searchService.searchPosition("FE");
//        Assertions.assertNotNull(result);
//        Assertions.assertFalse(result.isEmpty());
//    }
//
//    @Test
//    void searchPosition_wrongValue_shouldReturnEmpty() {
//        var result = searchService.searchPosition("Peter Danh");
//        Assertions.assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void searchCompetencyCycle_trueValue_shouldReturnResult() {
//        var result = searchService.searchCompetencyCycle("Competency Evaluation");
//        Assertions.assertNotNull(result);
//        Assertions.assertFalse(result.isEmpty());
//    }
//
//    @Test
//    void searchCompetencyCycle_wrongValue_shouldReturnEmpty() {
//        var result = searchService.searchCompetencyCycle("Peter Danh");
//        Assertions.assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void searchPerformanceCycle_trueValue_shouldReturnResult() {
//        var result = searchService.searchPerformanceCycle("Performace Evluation");
//        Assertions.assertNotNull(result);
//        Assertions.assertFalse(result.isEmpty());
//    }
//
//    @Test
//    void searchPerformanceCycle_wrongValue_shouldReturnEmpty() {
//        var result = searchService.searchPerformanceCycle("Peter Danh");
//        Assertions.assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void searchGlobal_trueValue_shouldReturnResult() {
//        var r1 = searchService.searchGlobal("Peter");
//        var r2 = searchService.searchGlobal("Englishz");
//        var r3 = searchService.searchGlobal("Ba");
//        var r4 = searchService.searchGlobal("qc");
//        var r5 = searchService.searchGlobal("Evluation");
//        var r6 = searchService.searchGlobal("Work");
//        Assertions.assertNotNull(r1);
//        Assertions.assertFalse(r1.isEmpty());
//        Assertions.assertNotNull(r2);
//        Assertions.assertFalse(r2.isEmpty());
//        Assertions.assertNotNull(r3);
//        Assertions.assertFalse(r3.isEmpty());
//        Assertions.assertNotNull(r4);
//        Assertions.assertFalse(r4.isEmpty());
//        Assertions.assertNotNull(r5);
//        Assertions.assertFalse(r5.isEmpty());
//        Assertions.assertNotNull(r6);
//        Assertions.assertFalse(r6.isEmpty());
//    }
//
//    @Test
//    void searchGlobal_wrongValue_shouldReturnEmpty() {
//        var result = searchService.searchGlobal("zxcvvbnm");
//        Assertions.assertTrue(result.isEmpty());
//    }
//}
