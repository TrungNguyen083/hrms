package com.hrms.global.controllers;

import com.hrms.careerpathmanagement.dto.TimeLine;
import com.hrms.employeemanagement.dto.SimpleItemDTO;
import com.hrms.global.models.*;
import com.hrms.global.services.CompanyCoreService;
import com.hrms.usermanagement.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @QueryMapping(name = "proficiencyLevels")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('HR')")
    public List<ProficiencyLevel> getProficiencyLevels() {
        return companyCoreService.getProficiencyLevels();
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
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('HR') or hasAnyAuthority('EMPLOYEE')")
    public List<EvaluateCycle> getEvaluateCycles() {
        try {
            return companyCoreService.getEvaluateCycles();
        } catch (Exception e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @QueryMapping(name = "evaluateTimeLine")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('HR')")
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
}
