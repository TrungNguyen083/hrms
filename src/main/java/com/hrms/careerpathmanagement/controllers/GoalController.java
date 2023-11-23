package com.hrms.careerpathmanagement.controllers;

import com.hrms.careerpathmanagement.dto.DiffPercentDTO;
import com.hrms.careerpathmanagement.dto.pagination.EmployeeGoalPagination;
import com.hrms.careerpathmanagement.services.GoalService;
import com.hrms.global.dto.PieChartDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class GoalController {
    @Autowired
    GoalService goalService;

    @QueryMapping("goalsByDepartmentAndCycle")
    public EmployeeGoalPagination getGoals(@Argument Integer departmentId,
                                           @Argument Integer cycleId,
                                           @Argument Integer pageNo,
                                           @Argument Integer pageSize)
    {
        return goalService.getEmployeesGoals(departmentId, cycleId, pageNo, pageSize);
    }

    public DiffPercentDTO getGoalsStatistic(@Argument Integer departmentId,
                                            @Argument Integer cycleId,
                                            @Argument Integer pageNo,
                                            @Argument Integer pageSize)
    {
        return goalService.countGoals(departmentId, cycleId, pageNo, pageSize);
    }

    @QueryMapping(name = "goalsStatusPieChart")
    public PieChartDTO getPieChartGoalsStatus(@Argument Integer departmentId, @Argument Integer cycleId) {
        return goalService.getGoalsStatusStatistic(departmentId, cycleId);
    }
}
