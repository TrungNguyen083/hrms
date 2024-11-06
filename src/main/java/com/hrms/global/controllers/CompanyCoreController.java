package com.hrms.global.controllers;

import com.hrms.careerpathmanagement.dto.TimeLine;
import com.hrms.global.input.CompetencyGroupInput;
import com.hrms.global.input.CompetencyInput;
import com.hrms.global.input.EvaluateCycleInput;
import com.hrms.employeemanagement.dto.SimpleItemDTO;
import com.hrms.global.models.*;
import com.hrms.global.services.CompanyCoreService;
import com.hrms.performancemanagement.input.PerformanceRangeInput;
import com.hrms.careerpathmanagement.input.ProficiencyLevelInput;
import com.hrms.performancemanagement.model.PerformanceRange;
import com.hrms.usermanagement.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@Slf4j
public class CompanyCoreController {
    private final CompanyCoreService companyCoreService;

    @Autowired
    public CompanyCoreController(CompanyCoreService companyCoreService) {
        this.companyCoreService = companyCoreService;
    }

    @QueryMapping(name = "departments")
    @PreAuthorize("hasAuthority('PM') or hasAuthority('EMPLOYEE') or hasAuthority('HR')")
    public List<Department> getDepartments() {
        return companyCoreService.getDepartments();
    }

    @QueryMapping(name = "NumberOfDepartments")
    @PreAuthorize("hasAuthority('PM') or hasAuthority('EMPLOYEE') or hasAuthority('HR')")
    public Long getNumberOfDepartments() {
        return companyCoreService.getNumberOfDepartments();
    }

    @QueryMapping(name = "jobLevels")
    @PreAuthorize("hasAuthority('PM') or hasAuthority('EMPLOYEE') or hasAuthority('HR')")
    public List<JobLevel> getJobLevels() {
        return companyCoreService.getJobLevels();
    }

    @QueryMapping(name = "competencies")
    @PreAuthorize("hasAuthority('PM') or hasAuthority('EMPLOYEE') or hasAuthority('HR') or hasAuthority('SUM')")
    public List<Competency> getCompetencies() { return companyCoreService.getCompetencies(); }

    @QueryMapping(name = "positions")
    @PreAuthorize("hasAuthority('PM') or hasAuthority('EMPLOYEE') or hasAuthority('HR')")
    public List<Position> getPositions() {
        return companyCoreService.getPositions();
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Role> roles() {
        return companyCoreService.getRoles();
    }

    @QueryMapping(name = "evaluateCycles")
    @PreAuthorize("hasAuthority('SUM') or hasAuthority('HR') or hasAnyAuthority('EMPLOYEE') or hasAuthority('PM')")
    public List<EvaluateCycle> getEvaluateCycles() {
        try {
            return companyCoreService.getEvaluateCycles();
        } catch (Exception e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @QueryMapping(name = "evaluateTimeLine")
    @PreAuthorize("hasAuthority('SUM') or hasAuthority('HR')")
    public List<TimeLine> getEvaluateTimeLine(@Argument Integer evaluateCycleId) {
        try {
            return companyCoreService.getEvaluateTimeline(evaluateCycleId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @QueryMapping(name = "positionLevelSkills")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER')")
    public List<SimpleItemDTO> getPositionLevelSkills(@Argument Integer positionId, @Argument Integer jobLevelId) {
        return companyCoreService.getPositionLevelSkills(positionId, jobLevelId);
    }

    @Transactional
    @MutationMapping(name = "createEvaluationCycle")
    @PreAuthorize("hasAuthority('HR')")
    public Boolean createEvaluationCycle(@Argument EvaluateCycleInput input) {
        return companyCoreService.createEvaluationCycle(input);
    }

    @QueryMapping(name = "proficiencyLevels")
    @PreAuthorize("hasAuthority('SUM') or hasAuthority('HR')")
    public List<ProficiencyLevel> getProficiencyLevels() {
        return companyCoreService.getProficiencyLevels();
    }

    @QueryMapping(name = "performanceRanges")
    @PreAuthorize("hasAuthority('HR')")
    public List<PerformanceRange> getPerformanceRanges() { return companyCoreService.getPerformanceRanges(); }

    @MutationMapping(name = "createProficiencyLevel")
    @PreAuthorize("hasAuthority('HR')")
    public Boolean createProficiencyLevel(@Argument ProficiencyLevelInput input)
    {
        return companyCoreService.createProficiencyLevel(input);
    }

    @MutationMapping(name = "createPerformanceRange")
    @PreAuthorize("hasAuthority('HR')")
    public Boolean createPerformanceRange(@Argument PerformanceRangeInput input) {
        return companyCoreService.createPerformanceRange(input);
    }

    @MutationMapping(name = "updateProficiencyLevel")
    @PreAuthorize("hasAuthority('HR')")
    public Boolean updateProficiencyLevel(@Argument Integer id, @Argument ProficiencyLevelInput input)
    {
        return companyCoreService.updateProficiencyLevel(id, input);
    }

    @MutationMapping(name = "updatePerformanceRange")
    @PreAuthorize("hasAuthority('HR')")
    public Boolean updatePerformanceRage(@Argument Integer id, @Argument PerformanceRangeInput input) {
        return companyCoreService.updatePerformanceRange(id, input);
    }

    @MutationMapping(name = "deleteProficiencyLevel")
    @PreAuthorize("hasAuthority('HR')")
    public Boolean deleteProficiencyLevel(@Argument Integer id) {
        return companyCoreService.deleteProficiencyLevel(id);
    }

    @MutationMapping(name = "deletePerformanceRange")
    @PreAuthorize("hasAuthority('HR')")
    public Boolean deletePerformanceRange(@Argument Integer id) {
        return companyCoreService.deletePerformanceRange(id);
    }

    @MutationMapping(name = "createCompetencyGroup")
    @PreAuthorize("hasAuthority('HR')")
    public Boolean createCompetencyGroup(@Argument CompetencyGroupInput input) {
        return companyCoreService.createCompetencyGroup(input);
    }

    @MutationMapping(name = "updateCompetencyGroup")
    @PreAuthorize("hasAuthority('HR')")
    public Boolean updateCompetencyGroup(@Argument Integer id, @Argument CompetencyGroupInput input)
    {
        return companyCoreService.updateCompetencyGroup(id, input);
    }

    @MutationMapping(name = "deleteCompetencyGroup")
    @PreAuthorize("hasAuthority('HR')")
    public Boolean deleteCompetencyGroup(@Argument Integer id) {
        return companyCoreService.deleteCompetencyGroup(id);
    }

    @MutationMapping(name = "createCompetency")
    @PreAuthorize("hasAuthority('HR')")
    public Boolean createCompetency(@Argument CompetencyInput input) {
        return companyCoreService.createCompetency(input);
    }

    @MutationMapping(name = "updateCompetency")
    @PreAuthorize("hasAuthority('HR')")
    public Boolean updateCompetency(@Argument Integer id, @Argument CompetencyInput input)
    {
        return companyCoreService.updateCompetency(id, input);
    }

    @MutationMapping(name = "deleteCompetency")
    @PreAuthorize("hasAuthority('HR')")
    public Boolean deleteCompetency(@Argument Integer id) {
        return companyCoreService.deleteCompetency(id);
    }
}
