package com.hrms.careerpathmanagement.services;

import com.hrms.careerpathmanagement.dto.ChartData;
import com.hrms.careerpathmanagement.dto.CompareGoal;
import com.hrms.careerpathmanagement.dto.CountAndPercentDTO;
import com.hrms.careerpathmanagement.dto.GoalProgressDTO;
import com.hrms.global.dto.PieChartDTO;

import java.util.List;

public interface GoalService {

    List<GoalProgressDTO> getDepartmentGoalProgress(Integer departmentId, Integer cycleId);

    List<CompareGoal> getCompareGoals(List<Integer> employeeIds, Integer cycleId);

    ChartData getCompareGoalPieChart(List<Integer> employeeIds, Integer cycleId);
}
