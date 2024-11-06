package com.hrms.performancemanagement.services;

import com.hrms.careerpathmanagement.dto.DiffPercentDTO;
import com.hrms.careerpathmanagement.dto.EmployeePotentialPerformanceDTO;
import com.hrms.global.models.EvaluateCycle;
import com.hrms.employeemanagement.dto.pagination.EmployeeRatingPagination;
import com.hrms.global.dto.BarChartDTO;
import com.hrms.global.dto.DataItemPagingDTO;
import com.hrms.global.dto.MultiBarChartDTO;
import com.hrms.global.dto.PieChartDTO;
import com.hrms.performancemanagement.dto.PerformanceCategoryRating;
import com.hrms.performancemanagement.dto.PerformanceOverall;
import com.hrms.performancemanagement.dto.PerformanceQuestionRating;
import com.hrms.performancemanagement.dto.StackedBarChart;
import com.hrms.performancemanagement.model.PerformanceEvaluationOverall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PerformanceService {
    Page<PerformanceEvaluationOverall> getPerformanceEvaluations(Integer cycleId, Pageable pageable);
    Float getAveragePerformanceScore(Integer cycleId);
    BarChartDTO performanceOverviewChart(Integer cycleId, Integer departmentId);
    StackedBarChart getPerformanceByJobLevel(Integer positionId, Integer cycleId);
    DiffPercentDTO performanceDiffPercent(Integer cycleId, Integer departmentId);
    List<EmployeePotentialPerformanceDTO> getPotentialAndPerformance(Integer departmentId, Integer cycleId);
    List<EmployeePotentialPerformanceDTO> getPotentialAndPerformanceByPosition(Integer departmentId, Integer cycleId);
    EmployeeRatingPagination getPerformanceRating(Integer departmentId, Integer cycleId, PageRequest pageable);
    DataItemPagingDTO getEmployeePerformanceRatingScore(Integer employeeId, Integer pageNo, Integer pageSize);
    PieChartDTO getPerformanceEvalProgress(Integer cycleId, Integer departmentId);
    String performanceCyclePeriod(Integer cycleId);
    PieChartDTO getPerformancePieChartOverall(EvaluateCycle cycleId);
    MultiBarChartDTO getCompletedEvaluationByPosition(Integer cycleId, Integer departmentId);

    PerformanceOverall getPerformanceOverall(Integer employeeId, Integer cycleId);

    List<PerformanceCategoryRating> getPerformanceCategoryRating(Integer employeeId, Integer cycleId);

    List<PerformanceQuestionRating> getPerformanceQuestionRating(Integer employeeId, Integer cycleId);

    PerformanceOverall getManagerPerformanceOverall(Integer employeeId, Integer cycleId);

    List<PerformanceCategoryRating> getManagerPerformanceCategoryRating(Integer employeeId, Integer cycleId);

    List<PerformanceQuestionRating> getManagerPerformanceQuestionRating(Integer employeeId, Integer cycleId);

    PerformanceOverall getFinalPerformanceOverall(Integer employeeId, Integer cycleId);

    List<PerformanceCategoryRating> getFinalPerformanceCategoryRating(Integer employeeId, Integer cycleId);

    List<PerformanceQuestionRating> getFinalPerformanceQuestionRating(Integer employeeId, Integer cycleId);
}
