package com.hrms.careerpathmanagement.controllers;

import com.hrms.careerpathmanagement.dto.ChartData;
import com.hrms.careerpathmanagement.dto.CompareGoal;
import com.hrms.careerpathmanagement.dto.CountAndPercentDTO;
import com.hrms.careerpathmanagement.dto.GoalProgressDTO;
import com.hrms.careerpathmanagement.dto.pagination.EmployeeGoalPagination;
import com.hrms.careerpathmanagement.dto.pagination.GoalPagination;
import com.hrms.careerpathmanagement.services.GoalService;
import com.hrms.global.dto.PieChartDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class GoalController {
    @Autowired
    private final GoalService goalService;

    @Autowired
    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @QueryMapping("departmentGoalProgress")
    @PreAuthorize("hasAuthority('SUM')")
    public List<GoalProgressDTO> getDepartmentGoalProgress(@Argument Integer departmentId, @Argument Integer cycleId) {
        return goalService.getDepartmentGoalProgress(departmentId, cycleId);
    }

    @QueryMapping("compareGoals")
    @PreAuthorize("hasAuthority('HR')")
    public List<CompareGoal> getCompareGoals(@Argument List<Integer> employeeIds, @Argument Integer cycleId) {
        return goalService.getCompareGoals(employeeIds, cycleId);
    }

    @QueryMapping("compareGoalPieChart")
    @PreAuthorize("hasAuthority('HR')")
    public ChartData getCompareGoalPieChart(@Argument List<Integer> employeeIds, @Argument Integer cycleId) {
        return goalService.getCompareGoalPieChart(employeeIds, cycleId);
    }
}
