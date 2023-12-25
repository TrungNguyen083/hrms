package com.hrms.careerpathmanagement.services;

import com.hrms.careerpathmanagement.PositionCareerPath;
import com.hrms.careerpathmanagement.models.PositionJobLevelSkillSet;
import com.hrms.careerpathmanagement.models.PositionLevelPath;
import com.hrms.careerpathmanagement.models.SkillSetEvaluation;
import com.hrms.careerpathmanagement.repositories.PositionJobLevelSkillSetRepository;
import com.hrms.careerpathmanagement.repositories.PositionLevelPathRepository;
import com.hrms.careerpathmanagement.repositories.SkillSetEvaluationRepository;
import com.hrms.employeemanagement.models.*;
import com.hrms.employeemanagement.repositories.JobLevelRepository;
import com.hrms.employeemanagement.repositories.PositionLevelRepository;
import com.hrms.employeemanagement.repositories.PositionRepository;
import com.hrms.employeemanagement.repositories.SkillSetRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class CareerManagementServiceIntegrationTest {
    @Autowired
    private JobLevelRepository jobLevelRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private PositionLevelRepository positionLevelRepository;

    @Autowired
    private PositionLevelPathRepository positionLevelPathRepository;

    @Autowired
    private PositionJobLevelSkillSetRepository positionJobLevelSkillSetRepository;

    @Autowired
    private SkillSetEvaluationRepository skillSetEvaluationRepository;

    @Autowired
    private SkillSetRepository skillSetRepository;
    @Autowired
    private EntityManager em;
    private CareerManagementService careerManagementService;

    static MySQLContainer mysql = new MySQLContainer<>("mysql:latest")
            .withExposedPorts(3306)
            .withDatabaseName("hrms")
            .withUsername("root")
            .withPassword("root");

    static ElasticsearchContainer elastic = new ElasticsearchContainer("elasticsearch:8.7.1")
            .withExposedPorts(9201);

    static {
        mysql.start();
        elastic.start();
    }

    @Test
    void testContextLoads() {
        assert mysql.isRunning() == true;
        assert elastic.isRunning() == true;
        assert positionLevelRepository != null;
        assert positionLevelPathRepository != null;
        assert positionJobLevelSkillSetRepository != null;
        assert skillSetEvaluationRepository != null;
    }

    @BeforeEach
    void init() {
        careerManagementService = new CareerManagementService(
                positionLevelPathRepository,
                positionLevelRepository,
                positionJobLevelSkillSetRepository,
                skillSetEvaluationRepository,
                em
        );

        Position pos1 = Position.builder()
                .id(1)
                .positionName("Software Engineer")
                .insertionTime(new Date())
                .modificationTime(new Date())
                .build();

        positionRepository.save(pos1);

        JobLevel level1 = JobLevel.builder()
                .id(1)
                .jobLevelName("Junior")
                .build();

        JobLevel level2 = JobLevel.builder()
                .id(2)
                .jobLevelName("Senior")
                .build();

        jobLevelRepository.saveAll(List.of(level1, level2));

        PositionLevel posLevel1 = PositionLevel.builder()
                .id(1)
                .position(pos1)
                .jobLevel(level1)
                .build();

        PositionLevel posLevel2 = PositionLevel.builder()
                .id(2)
                .position(pos1)
                .jobLevel(level2)
                .build();

        positionLevelRepository.saveAll(List.of(posLevel1, posLevel2));

        positionLevelPathRepository.save(PositionLevelPath.builder()
                .id(1)
                .current(posLevel1)
                .next(posLevel2)
                .build()
        );

        SkillSet skillSet1 = SkillSet.builder()
                .id(1)
                .skillSetName("Java")
                .insertionTime(new Date())
                .modificationTime(new Date())
                .build();

        SkillSet skillSet2 = SkillSet.builder()
                .id(2)
                .skillSetName("Python")
                .insertionTime(new Date())
                .modificationTime(new Date())
                .build();

        skillSetRepository.saveAll(List.of(skillSet1, skillSet2));

        PositionJobLevelSkillSet posJobLevelSkillSet1 = PositionJobLevelSkillSet.builder()
                .id(1)
                .position(pos1)
                .jobLevel(level1)
                .skillSet(skillSet1)
                .build();

        PositionJobLevelSkillSet posJobLevelSkillSet2 = PositionJobLevelSkillSet.builder()
                .id(2)
                .position(pos1)
                .jobLevel(level1)
                .skillSet(skillSet2)
                .build();

        positionJobLevelSkillSetRepository.saveAll(List.of(posJobLevelSkillSet1, posJobLevelSkillSet2));
    }
    @Test
    void getCareerPathTree() {
        PositionCareerPath position = PositionCareerPath.SOFTWARE_ENGINEER;
        var result = careerManagementService.getCareerPathTree(position);
        assert result.getTitle().equals("Software Engineer");
    }

    @Test
    void getPositionLevelNode() {
        var result = careerManagementService.getPositionLevelNode(1);
        assert result.getTitle().equals("Junior Software Engineer");
    }

    @Test
    void getMatchPercent() {
        Employee e = Employee.builder()
                .id(1)
                .build();

        skillSetEvaluationRepository.save(SkillSetEvaluation.builder()
                .id(1)
                .employee(e)
                .skillSet(skillSetRepository.findById(1).get())
                .selfScore(4)
                .evaluatorScore(4)
                .finalScore(4)
                .build());

        var matchPer = careerManagementService.getMatchPercent(1, 1, 1);
        assert matchPer > 0;
    }

    @Test
    void getBaselineSkillSetAvgScore() {
    }
}