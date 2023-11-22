package com.hrms.performancemanagement.controllers;

import com.hrms.careerpathmanagement.dto.EmployeePotentialPerformanceDTO;
import com.hrms.employeemanagement.dto.pagination.EmployeeRatingPagination;
import com.hrms.careerpathmanagement.dto.TimeLine;
import com.hrms.global.dto.DataItemPagingDTO;
import com.hrms.global.dto.MultiBarChartDTO;
import com.hrms.global.dto.PieChartDTO;
import com.hrms.performancemanagement.dto.StackedBarChart;
import com.hrms.performancemanagement.model.PerformanceEvaluation;
import com.hrms.performancemanagement.services.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@CrossOrigin(origins = "*")
public class PerformanceController {

    private final PerformanceService performanceService;

    @Autowired
    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @QueryMapping(name = "departmentInCompletePerform")
    public MultiBarChartDTO getDepartmentInCompletePerform(@Argument Integer performanceCycleId)
    {
        return performanceService.getDepartmentInCompletePerform(performanceCycleId);
    }

    @QueryMapping(name = "performanceEvalProgress")
    public PieChartDTO getPerformanceEvalProgress(@Argument Integer performanceCycleId)
    {
        return performanceService.getPerformanceEvalProgress(performanceCycleId);
    }

    @QueryMapping(name = "averagePerformanceScore")
    public Float getAveragePerformanceScore(@Argument Integer cycleId) {
        return performanceService.getAveragePerformanceScore(cycleId);
    }

    /**
     * HR Dashboard - Component: Performance by Job Level
     * @return a StackedBarChart, including 5 bars: Unsatisfactory, Needs Improvement, Meets Expectations, Exceeds Expectations, Outstanding
     */

    @QueryMapping(name = "performanceByJobLevel")
    public StackedBarChart getPerformanceByJobLevel(@Argument Integer positionId,
                                                    @Argument Integer cycleId)
    {
        return performanceService.getPerformanceByJobLevel(positionId, cycleId);
    }

    /**
     * HR Dashboard - Component: Employee Performance & Potential
     * @return Descartes coordinate, x-axis: performance, y-axis: potential
     */
    @QueryMapping(name = "employeesPotentialPerformance")
    public List<EmployeePotentialPerformanceDTO> getPotentialAndPerformance(@Argument Integer departmentId,
                                                                            @Argument Integer cycleId)
    {
        return performanceService.getPotentialAndPerformance(departmentId, cycleId);
    }


    /**
     * HR Dashboard - Component: Top Performers
     * @return List Employees (name, profileImg) with their performance rating
     */

    @QueryMapping(name = "topPerformers")
    public EmployeeRatingPagination getPerformanceRating(@Argument Integer cycleId,
                                                         @Argument Integer pageNo,
                                                         @Argument Integer pageSize)
    {
        Sort sort = Sort.by(Sort.Direction.DESC, "finalAssessment");
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return performanceService.getPerformanceRating(cycleId, pageable);
    }

    @QueryMapping
    public Page<PerformanceEvaluation> getPerformanceEvaluations(@Argument Integer cycleId,
                                                                 @Argument int pageNo,
                                                                 @Argument int pageSize)
    {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return performanceService.getPerformanceEvaluations(cycleId, pageable);
    }

    /***
     * Employee Dashboard - Component: Performance Rating Scores
     * @return BarChart
     */
    @QueryMapping(name = "employeePerformanceRatingScore")
    public DataItemPagingDTO getEmployeePerformanceRatingScore(@Argument Integer employeeId,
                                                                     @Argument int pageNo,
                                                                     @Argument int pageSize)
    {
        return performanceService.getEmployeePerformanceRatingScore(employeeId, pageNo, pageSize);
    }

    @QueryMapping(name = "performanceTimeLine")
    public List<TimeLine> getPerformanceTimeLine(@Argument Integer performanceCycleId)
    {
        return performanceService.getPerformanceTimeLine(performanceCycleId);
    }

}