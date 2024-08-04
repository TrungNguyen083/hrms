package com.hrms.careerpathmanagement.services;

import com.hrms.careerpathmanagement.repositories.PositionLevelSkillSetRepository;
import com.hrms.careerpathmanagement.repositories.PositionLevelPathRepository;
import com.hrms.careerpathmanagement.repositories.SkillSetEvaluationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class CareerManagementServiceTest {

    private CareerManagementService careerManagementService;
    private PositionLevelPathRepository positionLevelPathRepository;
    private PositionLevelSkillSetRepository baselineSkillSetRepository;
    private SkillSetEvaluationRepository skillSetEvaluationRepository;

    @BeforeEach
    void init() {
        positionLevelPathRepository = mock(PositionLevelPathRepository.class);
        careerManagementService = new CareerManagementService(
                positionLevelPathRepository,
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