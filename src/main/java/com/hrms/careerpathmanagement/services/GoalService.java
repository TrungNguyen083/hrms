package com.hrms.careerpathmanagement.services;

import com.hrms.careerpathmanagement.dto.CountAndPercentDTO;
import com.hrms.careerpathmanagement.dto.GoalProgressDTO;
import com.hrms.global.dto.PieChartDTO;

import java.util.List;

public interface GoalService {

    List<GoalProgressDTO> getDepartmentGoalProgress(Integer departmentId, Integer cycleId);

    CountAndPercentDTO countGoalsCompleted(Integer departmentId, Integer cycleId);

    PieChartDTO getGoalsStatusStatistic(Integer departmentId, Integer cycleId);
}
