package com.hrms.careerpathmanagement.controllers;

import com.hrms.careerpathmanagement.dto.CountAndPercentDTO;
import com.hrms.careerpathmanagement.dto.pagination.EmployeeGoalPagination;
import com.hrms.careerpathmanagement.dto.pagination.GoalPagination;
import com.hrms.careerpathmanagement.services.GoalService;
import com.hrms.global.dto.PieChartDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
public class GoalController {
    @Autowired
    GoalService goalService;

    //SUM Dashboard - Component: Competency Evaluation Status &
    @QueryMapping("goalsByDepartmentAndCycle")
    @PreAuthorize("hasAuthority('MANAGER')")
    public EmployeeGoalPagination getGoals(@Argument Integer departmentId,
                                           @Argument Integer cycleId,
                                           @Argument Integer pageNo,
                                           @Argument Integer pageSize)
    {
        return goalService.getEmployeesGoals(departmentId, cycleId, pageNo, pageSize);
    }

    // SUM Dashboard - Component: Goal Achievement
    @QueryMapping(name = "goalsCountingStatistic")
    @PreAuthorize("hasAuthority('MANAGER')")
    public CountAndPercentDTO getGoalsStatistic(@Argument Integer departmentId,
                                                @Argument Integer cycleId)
    {
        return goalService.countGoalsCompleted(departmentId, cycleId);
    }

    // SUM Dashboard - Component: Goal Achievement
    @QueryMapping(name = "goalsStatusPieChart")
    @PreAuthorize("hasAuthority('MANAGER')")
    public PieChartDTO getPieChartGoalsStatus(@Argument Integer departmentId, @Argument Integer cycleId) {
        return goalService.getGoalsStatusStatistic(departmentId, cycleId);
    }

    @QueryMapping(name = "goalsByEmployee")
    @PreAuthorize("hasAnyAuthority('USER') or hasAuthority('MANAGER')")
    public GoalPagination trackGoals(@Argument Integer employeeId,
                                     @Argument Integer pageNo,
                                     @Argument Integer pageSize)
    {
        return goalService.getGoals(employeeId, pageNo, pageSize);
    }
}
