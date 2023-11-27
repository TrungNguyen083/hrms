package com.hrms.performancemanagement.services;

import com.hrms.careerpathmanagement.dto.DiffPercentDTO;
import com.hrms.careerpathmanagement.dto.EmployeePotentialPerformanceDTO;
<<<<<<< HEAD
import com.hrms.careerpathmanagement.models.ProficiencyLevel;
=======
import com.hrms.careerpathmanagement.input.EvaluationProcessInput;
>>>>>>> db1335d1e29b29ab86ec1ee05a6164e243302377
import com.hrms.employeemanagement.dto.pagination.EmployeeRatingPagination;
import com.hrms.careerpathmanagement.dto.TimeLine;
import com.hrms.global.dto.BarChartDTO;
import com.hrms.global.dto.DataItemPagingDTO;
import com.hrms.global.dto.MultiBarChartDTO;
import com.hrms.global.dto.PieChartDTO;
import com.hrms.performancemanagement.dto.StackedBarChart;
import com.hrms.performancemanagement.input.PerformanceCycleInput;
import com.hrms.performancemanagement.input.PerformanceRangeInput;
import com.hrms.performancemanagement.input.ProficiencyLevelInput;
import com.hrms.performancemanagement.model.PerformanceEvaluation;
import com.hrms.performancemanagement.model.PerformanceCycle;
import com.hrms.performancemanagement.model.PerformanceRange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PerformanceService {
    List<PerformanceCycle> getAllPerformanceCycles();
    Page<PerformanceEvaluation> getPerformanceEvaluations(Integer cycleId, Pageable pageable);
    Float getAveragePerformanceScore(Integer cycleId);
    BarChartDTO getPerformanceRatingScheme(Integer cycleId, Integer departmentId);
    StackedBarChart getPerformanceByJobLevel(Integer positionId, Integer cycleId);
    DiffPercentDTO getPerformanceOverview(Integer cycleId, Integer departmentId);
    List<EmployeePotentialPerformanceDTO> getPotentialAndPerformance(Integer departmentId, Integer cycleId);
    List<EmployeePotentialPerformanceDTO> getPotentialAndPerformanceByPosition(Integer departmentId, Integer cycleId, Integer positionId);
    EmployeeRatingPagination getPerformanceRating(Integer departmentId, Integer cycleId, PageRequest pageable);
    DataItemPagingDTO getEmployeePerformanceRatingScore(Integer employeeId, Integer pageNo, Integer pageSize);
    MultiBarChartDTO getDepartmentInCompletePerform(Integer cycleId);
    PieChartDTO getPerformanceEvalProgress(Integer performanceCycleId);

    List<TimeLine> getPerformanceTimeLine(Integer cycleId);

    PerformanceCycle createPerformanceCycle(PerformanceCycleInput input);

<<<<<<< HEAD
    ProficiencyLevel updateProficiencyLevel(Integer id, ProficiencyLevelInput input);

    PerformanceRange updatePerformanceRange(Integer id, PerformanceRangeInput input);
=======
    String performanceCyclePeriod(Integer cycleId);

    List<TimeLine> createPerformanceProcess(EvaluationProcessInput input);
>>>>>>> db1335d1e29b29ab86ec1ee05a6164e243302377
}
