package com.hrms.employeecompetency.controllers;

import com.hrms.employeecompetency.dto.*;
import com.hrms.employeecompetency.exceptions.CompetencyCycleNotFoundException;
import com.hrms.employeecompetency.models.*;
import com.hrms.employeecompetency.services.*;
import com.hrms.employeecompetency.specifications.CompetencyCycleSpecifications;
import com.hrms.employeecompetency.specifications.CompetencyEvaluationSpecifications;
import com.hrms.employeecompetency.specifications.EvaluationOverallSpecifications;
import com.hrms.employeecompetency.specifications.SkillSetEvaluationSpecifications;
import com.hrms.employeemanagement.exception.EmployeeNotFoundException;
import com.hrms.employeemanagement.models.Department;
import com.hrms.employeemanagement.models.Employee;
import com.hrms.employeemanagement.models.JobLevel;
import com.hrms.employeemanagement.paging.Pagination;
import com.hrms.employeemanagement.services.DepartmentService;
import com.hrms.employeemanagement.services.EmployeeService;
import com.hrms.employeemanagement.services.JobLevelService;
import com.hrms.employeemanagement.specifications.EmployeeSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class CompetencyGraphql {
    CompetencyCycleService competencyCycleService;
    DepartmentService departmentService;
    EmployeeService employeeService;
    EvaluationOverallService evaluationOverallService;
    CompetencyService competencyService;
    ProficiencyLevelService proficiencyLevelService;
    CompetencyEvaluationService competencyEvaluationService;
    JobLevelService jobLevelService;
    SkillSetEvaluationService skillSetEvaluationService;

    @Autowired
    public CompetencyGraphql(CompetencyCycleService competencyCycleService, DepartmentService departmentService,
                             EmployeeService employeeService, EvaluationOverallService evaluationOverallService,
                             CompetencyService competencyService, ProficiencyLevelService proficiencyLevelService,
                             CompetencyEvaluationService competencyEvaluationService, JobLevelService jobLevelService,
                             SkillSetEvaluationService skillSetEvaluationService) {
        this.competencyCycleService = competencyCycleService;
        this.departmentService = departmentService;
        this.employeeService = employeeService;
        this.evaluationOverallService = evaluationOverallService;
        this.competencyService = competencyService;
        this.proficiencyLevelService = proficiencyLevelService;
        this.competencyEvaluationService = competencyEvaluationService;
        this.jobLevelService = jobLevelService;
        this.skillSetEvaluationService = skillSetEvaluationService;
    }

    @QueryMapping(name = "competencyCycles")
    public List<CompetencyCycle> getCompetencyCycles() {
        return competencyCycleService.findAll(Specification.allOf());
    }

    @QueryMapping(name = "departmentInComplete")
    public List<DepartmentInComplete> getAllDepartmentInComplete(@Argument Integer compeCycleId) {
        List<DepartmentInComplete> departmentInCompletes = new ArrayList<>();
        List<Department> departments = departmentService.findAll(Specification.allOf());
        for (Department department : departments) {
            List<Employee> employees = employeeService.findAll(EmployeeSpecifications.hasDepartmentId(department.getId()));
            float empPercent = getPercentageOfEmployees(compeCycleId, employees);
            float evaPercent = getPercentageOfEvaluator(compeCycleId, employees);
            DepartmentInComplete departmentInComplete = new DepartmentInComplete(department, empPercent, evaPercent);
            departmentInCompletes.add(departmentInComplete);
        }
        return departmentInCompletes;
    }

    @QueryMapping(name = "companyInComplete")
    public Float getCompanyInCompletePercentage(@Argument Integer competencyCycleId) {
        List<Employee> employees = employeeService.findAll(Specification.allOf());
        return getPercentageOfEmployees(competencyCycleId, employees);
    }

    //Get percentage of input employees who has not completed evaluation
    private Float getPercentageOfEmployees(Integer competencyCycleId, List<Employee> employees) {
        int employeeHasCompleted = 0;
        for (Employee employee : employees) {
            if (!evaluationOverallService
                    .findAll(
                            EvaluationOverallSpecifications
                                    .hasEmployeeIdAndEmployeeStatusCompleted(employee.getId(), competencyCycleId)
                    ).isEmpty()) employeeHasCompleted += 1;
        }
        int employeeHasNotCompleted = employees.size() - employeeHasCompleted;
        return (float) employeeHasNotCompleted / employees.size() * 100;
    }

    private Float getPercentageOfEvaluator(Integer competencyCycleId, List<Employee> employees) {
        int employeeHasCompleted = 0;
        for (Employee employee : employees) {
            if (!evaluationOverallService
                    .findAll(
                            EvaluationOverallSpecifications
                                    .hasEmployeeIdAndEvaluatorStatusCompleted(employee.getId(), competencyCycleId)
                    ).isEmpty()) employeeHasCompleted += 1;
        }
        int employeeHasNotCompleted = employees.size() - employeeHasCompleted;
        return (float) employeeHasNotCompleted / employees.size() * 100;
    }

    @QueryMapping(name = "competencies")
    public List<Competency> getCompetencies() {
        return competencyService.findAll(Specification.allOf());
    }

    @QueryMapping(name = "proficiencyLevels")
    public List<ProficiencyLevel> getProficiencyLevels() {
        return proficiencyLevelService.findAll(Specification.allOf());
    }

    @QueryMapping(name = "avgCompetencyScore")
    public List<AvgCompetency> getAvgCompetencyScore(@Argument Integer positionId, @Argument Integer competencyCycleId) {
        List<AvgCompetency> avgCompetencies = new ArrayList<>();
        List<CompetencyEvaluation> competencyEvaluationsByPositionAndCycle = competencyEvaluationService
                .findAll(CompetencyEvaluationSpecifications.hasPositionAndCompetencyCycle(positionId, competencyCycleId));
        List<JobLevel> jobLevels = jobLevelService.findAll(Specification.allOf());
        Collections.reverse(jobLevels);
        List<Competency> competencies = competencyService.findAll(Specification.allOf());
        for (int i = 0; i < jobLevels.size(); i++) {
            for (int j = 0; j < competencies.size(); j++) {
                float avgScore;
                int finalI = i;
                int finalJ = j;
                List<CompetencyEvaluation> evaluationsHasJobLevelAndCompetency = competencyEvaluationsByPositionAndCycle.stream()
                        .filter(competencyEvaluation -> competencyEvaluation
                                .getEmployee().getPositionLevel().getJobLevel().getId() == jobLevels.get(finalI).getId()
                                && competencyEvaluation.getCompetency().getId().equals(competencies.get(finalJ).getId()))
                        .toList();
                if (evaluationsHasJobLevelAndCompetency.isEmpty()) {
                    avgScore = 0;
                } else {
                    int totalScore = 0;
                    for (CompetencyEvaluation competencyEvaluation : evaluationsHasJobLevelAndCompetency) {
                        totalScore += competencyEvaluation.getProficiencyLevel().getWeight();
                    }
                    avgScore = (float) totalScore / evaluationsHasJobLevelAndCompetency.size();
                }
                AvgCompetency avgCompetency = new AvgCompetency(jobLevels.get(i), competencies.get(j), avgScore);
                avgCompetencies.add(avgCompetency);
            }
        }
        return avgCompetencies;
    }

    @QueryMapping(name = "competencyRadarChart")
    public RadarChart getCompetencyRadarChart(@Argument List<Integer> competencyCyclesId, @Argument Integer departmentId) throws CompetencyCycleNotFoundException {
        List<Competency> competencies = competencyService.findAll(Specification.allOf());
        List<RadarDataset> listDataset = new ArrayList<>();
        for (Integer competencyCycleId : competencyCyclesId) {
            List<CompetencyEvaluation> competencyEvaluations = competencyEvaluationService
                    .findAll(CompetencyEvaluationSpecifications.hasCompetencyCycleAndHasEmployeeInDepartment(competencyCycleId, departmentId));
            List<Float> listAvgCompetencyScore = new ArrayList<>();
            for (Competency competency : competencies) {
                List<CompetencyEvaluation> competencyEvaluationsByCompetency = competencyEvaluations.stream()
                        .filter(competencyEvaluation -> competencyEvaluation.getCompetency().getId().equals(competency.getId()))
                        .toList();
                float avgScore;
                if (competencyEvaluationsByCompetency.isEmpty()) {
                    avgScore = 0;
                }
                else {
                    int totalScore = 0;
                    for (CompetencyEvaluation competencyEvaluation : competencyEvaluationsByCompetency) {
                        if(competencyEvaluation.getProficiencyLevel() == null) continue;
                        totalScore += competencyEvaluation.getProficiencyLevel().getWeight();
                    }
                    avgScore = (float) totalScore / competencyEvaluationsByCompetency.size();
                }
                listAvgCompetencyScore.add(avgScore);
            }
            CompetencyCycle competencyCycle = competencyCycleService
                    .findAll(CompetencyCycleSpecifications.hasId(competencyCycleId))
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new CompetencyCycleNotFoundException("Cycle not found with id: " + competencyCycleId));
            listDataset.add(new RadarDataset(competencyCycle.getCompetencyCycleName(), listAvgCompetencyScore));
        }
        List<String> labels = competencies.stream().map(Competency::getCompetencyName).toList();
        return new RadarChart(labels, listDataset);
    }

    @QueryMapping(name = "topHighestSkillSet")
    public TopHighestSkillSetPaging getTopHighestSkill(@Argument Integer competencyCycleId, @Argument int pageNo, @Argument int pageSize)
    {
        List<TopHighestSkillSet> topHighestSkillSets = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<SkillSetEvaluation> skillSetEvaluations =
                skillSetEvaluationService
                        .findAll(SkillSetEvaluationSpecifications.hasTop10ProficiencyLevelInCycle(competencyCycleId),pageable);
        for (SkillSetEvaluation skillSetEvaluation : skillSetEvaluations) {
            topHighestSkillSets.add(
                    new TopHighestSkillSet(skillSetEvaluation.getEmployee(),
                            skillSetEvaluation.getPositionSkillSet().getSkillSet(),
                            skillSetEvaluation.getFinalProficiencyLevel()));
        }
        long totalCount = skillSetEvaluations.getTotalElements();
        long numberOfPages = (long) Math.ceil(((double) totalCount) / pageSize);
        Pagination pagination = new Pagination(pageNo, pageSize, totalCount, numberOfPages);
        return new TopHighestSkillSetPaging(topHighestSkillSets, pagination, totalCount);
    }
}
