package com.hrms.careerpathmanagement.services;

import com.hrms.careerpathmanagement.models.PositionLevelPath;
import com.hrms.careerpathmanagement.repositories.PositionJobLevelSkillSetRepository;
import com.hrms.careerpathmanagement.repositories.PositionLevelPathRepository;
import com.hrms.careerpathmanagement.repositories.SkillSetEvaluationRepository;
import com.hrms.employeemanagement.models.Employee;
import com.hrms.employeemanagement.models.JobLevel;
import com.hrms.employeemanagement.models.Position;
import com.hrms.employeemanagement.models.PositionLevel;
import com.hrms.employeemanagement.repositories.PositionLevelRepository;
import jakarta.persistence.EntityManager;
import kotlinx.coroutines.Job;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CareerManagementServiceTest {
    private EntityManager em;

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
                skillSetEvaluationRepository,
                em
        );
        Position position1 = new Position();
        position1.setId(1);
        position1.setPositionName("Software Engineer");

        JobLevel jobLevel1 = new JobLevel();
        jobLevel1.setId(1);
        jobLevel1.setJobLevelName("Junior");

        JobLevel jobLevel2 = new JobLevel();
        jobLevel2.setId(2);
        jobLevel2.setJobLevelName("Senior");


        PositionLevel positionLevel1 = new PositionLevel();
        positionLevel1.setId(1);
        positionLevel1.setPosition(position1);
        positionLevel1.setJobLevel(jobLevel1);

        PositionLevel positionLevel2 = new PositionLevel();
        positionLevel2.setId(2);
        positionLevel2.setPosition(position1);
        positionLevel2.setJobLevel(jobLevel2);

        PositionLevelPath positionLevelPath = new PositionLevelPath();
        positionLevelPath.setId(1);
        positionLevelPath.setCurrent(positionLevel1);
        positionLevelPath.setNext(positionLevel2);

        when(positionLevelRepository.findById(1)).thenReturn(Optional.of(positionLevel1));
        when(positionLevelRepository.findById(2)).thenReturn(Optional.of(positionLevel2));

        when(positionLevelPathRepository.findAllByCurrentId(1)).thenReturn(List.of(positionLevelPath));
        when(positionLevelPathRepository.existsByCurrentId(1)).thenReturn(true);
        when(positionLevelPathRepository.existsByCurrentId(2)).thenReturn(false);

        Employee employee = new Employee();
        employee.setId(1);
        employee.setFirstName("first");
        employee.setLastName("last");
        employee.setPosition(position1);
        employee.setJobLevel(jobLevel1);
    }

    @Test
    void getCareerPathTree() {
    }

    @Test
    void getPositionLevelNode() {
        var positionLevelNode = careerManagementService.getPositionLevelNode(1);

        verify(positionLevelRepository, times(1)).findById(1);
        verify(positionLevelPathRepository, times(1)).existsByCurrentId(1);

        assert positionLevelNode != null;
    }

    @Test
    void getMatchPercent() {

    }

    @Test
    void getBaselineSkillSetAvgScore() {
    }
}