package com.hrms.careerpathmanagement.services;

import com.hrms.careerpathmanagement.repositories.PositionJobLevelSkillSetRepository;
import com.hrms.careerpathmanagement.repositories.PositionLevelPathRepository;
import com.hrms.careerpathmanagement.repositories.SkillSetEvaluationRepository;
import com.hrms.employeemanagement.repositories.PositionLevelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CareerManagementServiceTest {

    private CareerManagementService careerManagementService;
    private PositionLevelPathRepository positionLevelPathRepository;
    private PositionLevelRepository positionLevelRepository;
    private PositionJobLevelSkillSetRepository baselineSkillSetRepository;
    private SkillSetEvaluationRepository skillSetEvaluationRepository;

    @BeforeEach
    void init() {
        positionLevelPathRepository = mock(PositionLevelPathRepository.class);
        positionLevelRepository = mock(PositionLevelRepository.class);
        careerManagementService = new CareerManagementService(
                positionLevelPathRepository,
                positionLevelRepository,
                baselineSkillSetRepository,
                skillSetEvaluationRepository
        );


    }

    @Test
    void getCareerPathTree() {
    }

    @Test
    void getPositionLevelNode() {
    }

    @Test
    void getMatchPercent() {
    }

    @Test
    void getBaselineSkillSetAvgScore() {
    }
}