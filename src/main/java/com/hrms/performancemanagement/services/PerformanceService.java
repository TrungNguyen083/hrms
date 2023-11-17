package com.hrms.performancemanagement.services;

import com.hrms.careerpathmanagement.dto.EmployeePotentialPerformanceDTO;
import com.hrms.employeemanagement.dto.EmployeeRatingPagination;
import com.hrms.global.dto.DataItemPagingDTO;
import com.hrms.performancemanagement.dto.PerformanceByJobLevalChartDTO;
import com.hrms.performancemanagement.model.PerformanceEvaluation;
import com.hrms.performancemanagement.model.PerformanceCycle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PerformanceService {
    List<PerformanceCycle> getAllPerformanceCycles();
    Page<PerformanceEvaluation> getPerformanceEvaluations(Integer cycleId, Pageable pageable);
    PerformanceByJobLevalChartDTO getPerformanceByJobLevel(Integer positionId, Integer cycleId);
    List<EmployeePotentialPerformanceDTO> getPotentialAndPerformance(Integer departmentId, Integer cycleId);

    EmployeeRatingPagination getPerformanceRating(Integer cycleId, PageRequest pageable);

    DataItemPagingDTO getEmployeePerformanceRatingScore(Integer employeeId, Integer pageNo, Integer pageSize);
}
