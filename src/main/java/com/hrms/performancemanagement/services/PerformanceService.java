package com.hrms.performancemanagement.services;

import com.hrms.careerpathmanagement.dto.DiffPercentDTO;
import com.hrms.careerpathmanagement.dto.EmployeePotentialPerformanceDTO;
import com.hrms.global.models.EvaluateCycle;
import com.hrms.global.models.ProficiencyLevel;
import com.hrms.careerpathmanagement.input.EvaluationProcessInput;
import com.hrms.employeemanagement.dto.pagination.EmployeeRatingPagination;
import com.hrms.careerpathmanagement.dto.TimeLine;
import com.hrms.global.dto.BarChartDTO;
import com.hrms.global.dto.DataItemPagingDTO;
import com.hrms.global.dto.MultiBarChartDTO;
import com.hrms.global.dto.PieChartDTO;
import com.hrms.performancemanagement.dto.StackedBarChart;
import com.hrms.performancemanagement.input.PerformanceRangeInput;
import com.hrms.performancemanagement.input.ProficiencyLevelInput;
import com.hrms.performancemanagement.model.PerformanceEvaluationOverall;
import com.hrms.performancemanagement.model.PerformanceRange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.util.List;

public interface PerformanceService {
    Page<PerformanceEvaluationOverall> getPerformanceEvaluations(Integer cycleId, Pageable pageable);
    Float getAveragePerformanceScore(Integer cycleId);
    BarChartDTO performanceOverviewChart(Integer cycleId, Integer departmentId);
    StackedBarChart getPerformanceByJobLevel(Integer positionId, Integer cycleId);
    DiffPercentDTO performanceDiffPercent(Integer cycleId, Integer departmentId);
    List<EmployeePotentialPerformanceDTO> getPotentialAndPerformance(Integer departmentId, Integer cycleId);
    List<EmployeePotentialPerformanceDTO> getPotentialAndPerformanceByPosition(Integer departmentId, Integer cycleId, Integer positionId);
    EmployeeRatingPagination getPerformanceRating(Integer departmentId, Integer cycleId, PageRequest pageable);
    DataItemPagingDTO getEmployeePerformanceRatingScore(Integer employeeId, Integer pageNo, Integer pageSize);
    MultiBarChartDTO getDepartmentInCompletePerform(Integer cycleId);
    PieChartDTO getPerformanceEvalProgress(Integer performanceCycleId);
    String performanceCyclePeriod(Integer cycleId);
    PieChartDTO getPerformancePieChartOverall(EvaluateCycle cycleId);
}
