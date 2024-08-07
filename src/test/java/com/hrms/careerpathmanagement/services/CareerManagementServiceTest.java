//package com.hrms.careerpathmanagement.services;
//
//import com.hrms.careerpathmanagement.repositories.PositionLevelSkillRepository;
//import com.hrms.careerpathmanagement.repositories.PositionLevelPathRepository;
//import com.hrms.careerpathmanagement.repositories.SkillEvaluationRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.mockito.Mockito.mock;
//
//class CareerManagementServiceTest {
//
//    private CareerManagementService careerManagementService;
//    private PositionLevelPathRepository positionLevelPathRepository;
//    private PositionLevelSkillRepository baselineSkillSetRepository;
//    private SkillEvaluationRepository skillEvaluationRepository;
//
//    @BeforeEach
//    void init() {
//        positionLevelPathRepository = mock(PositionLevelPathRepository.class);
//        careerManagementService = new CareerManagementService(
//                positionLevelPathRepository,
//                baselineSkillSetRepository,
//                skillEvaluationRepository
//        );
//
//
//    }
//
//    @Test
//    void getCareerPathTree() {
//    }
//
//    @Test
//    void getPositionLevelNode() {
//    }
//
//    @Test
//    void getMatchPercent() {
//    }
//
//    @Test
//    void getBaselineSkillSetAvgScore() {
//    }
//}