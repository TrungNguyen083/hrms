package com.hrms.performancemanagement.controllers;

import com.hrms.careerpathmanagement.dto.ChartData;
import com.hrms.careerpathmanagement.dto.DiffPercentDTO;
import com.hrms.careerpathmanagement.dto.EmployeePotentialPerformanceDTO;
import com.hrms.careerpathmanagement.dto.pagination.EvaluationPaging;
import com.hrms.employeemanagement.dto.pagination.EmployeeRatingPagination;
import com.hrms.global.dto.BarChartDTO;
import com.hrms.global.dto.DataItemPagingDTO;
import com.hrms.global.dto.MultiBarChartDTO;
import com.hrms.global.dto.PieChartDTO;
import com.hrms.performancemanagement.dto.*;
import com.hrms.performancemanagement.input.PerformanceEvaluationInput;
import com.hrms.performancemanagement.model.PerformanceEvaluationOverall;
import com.hrms.performancemanagement.services.PerformanceService;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class PerformanceController {

    private final PerformanceService performanceService;

    @Autowired
    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    /**
     * HR Dashboard
     *
     */

    @QueryMapping(name = "performanceByJobLevel")
    @PreAuthorize("hasAuthority('PM') or hasAuthority('HR')")
    public StackedBarChart getPerformanceByJobLevel(@Argument Integer positionId,
                                                    @Argument Integer cycleId) {
        return performanceService.getPerformanceByJobLevel(positionId, cycleId);
    }

    @QueryMapping(name = "employeesPotentialPerformance")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('HR')")
    public List<EmployeePotentialPerformanceDTO> getPotentialAndPerformance(@Argument @Nullable Integer departmentId,
                                                                            @Argument Integer cycleId) {
        return performanceService.getPotentialAndPerformance(departmentId, cycleId);
    }

    @QueryMapping(name = "topPerformers")
    @PreAuthorize("hasAuthority('HR') or hasAuthority('SUM')")
    public EmployeeRatingPagination getPerformanceRating(@Argument @Nullable Integer departmentId,
                                                         @Argument Integer cycleId,
                                                         @Argument Integer pageNo,
                                                         @Argument Integer pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "finalAssessment");
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return performanceService.getPerformanceRating(departmentId, cycleId, pageable);
    }

    @QueryMapping(name = "performanceDiffPercent")
    @PreAuthorize("hasAuthority('SUM') or hasAuthority('HR')")
    public DiffPercentDTO performanceDiffPercent(@Argument Integer cycleId,
                                                 @Argument Integer departmentId)
    {
        return performanceService.performanceDiffPercent(cycleId, departmentId);
    }

    @QueryMapping(name = "performanceOverviewChart")
    @PreAuthorize("hasAuthority('SUM') or hasAuthority('HR')")
    public BarChartDTO performanceOverviewChart(@Argument @Nullable Integer departmentId,
                                                @Argument Integer cycleId)
    {
        return performanceService.performanceOverviewChart(cycleId, departmentId);
    }



    /**
     * Employee Dashboard
     *
     */

    @QueryMapping(name = "employeePerformanceRatingScore")
    @PreAuthorize("hasAuthority('PM') or hasAuthority('EMPLOYEE')")
    public DataItemPagingDTO getEmployeePerformanceRatingScore(@Argument Integer employeeId,
                                                               @Argument int pageNo,
                                                               @Argument int pageSize) {
        return performanceService.getEmployeePerformanceRatingScore(employeeId, pageNo, pageSize);
    }



    /**
     * SUM Dashboard
     *
     */

    @QueryMapping(name = "departmentPotentialAndPerformance")
    @PreAuthorize("hasAuthority('SUM')")
    public List<EmployeePotentialPerformanceDTO> getPotentialAndPerformanceByPosition(@Argument Integer departmentId,
                                                                                      @Argument Integer cycleId) {
        return performanceService.getPotentialAndPerformanceByPosition(departmentId, cycleId);
    }

    @QueryMapping(name = "completedPerformEvaluationByPosition")
    @PreAuthorize("hasAuthority('SUM')")
    public MultiBarChartDTO getCompletedEvaluationByPosition(@Argument Integer cycleId, @Argument Integer departmentId) {
        return performanceService.getCompletedEvaluationByPosition(cycleId, departmentId);
    }

    @QueryMapping(name = "performanceEvaluationProgressPieChart")
    @PreAuthorize("hasAuthority('SUM')")
    public PieChartDTO getPerformanceEvalProgress(@Argument Integer cycleId, @Argument Integer departmentId) {
        return performanceService.getPerformanceEvalProgress(cycleId, departmentId);
    }


    /**
     * Performance Evaluation
     *
     */

    @QueryMapping(name = "performanceOverall")
    @PreAuthorize("hasAuthority('SUM') or hasAuthority('EMPLOYEE')")
    public PerformanceOverall getPerformanceOverall(@Argument Integer employeeId, @Argument Integer cycleId) {
        return performanceService.getPerformanceOverall(employeeId, cycleId);
    }

    @QueryMapping(name = "performanceCategoryRating")
    @PreAuthorize("hasAuthority('SUM') or hasAuthority('EMPLOYEE')")
    public List<PerformanceCategoryRating> getPerformanceCategoryRating(@Argument Integer employeeId, @Argument Integer cycleId) {
        return performanceService.getPerformanceCategoryRating(employeeId, cycleId);
    }

    @QueryMapping(name = "performanceQuestionRating")
    @PreAuthorize("hasAuthority('SUM') or hasAuthority('EMPLOYEE')")
    public List<PerformanceQuestionRating> getPerformanceQuestionRating(@Argument Integer employeeId, @Argument Integer cycleId) {
        return performanceService.getPerformanceQuestionRating(employeeId, cycleId);
    }

    @QueryMapping(name = "managerPerformanceOverall")
    @PreAuthorize("hasAuthority('SUM') or hasAuthority('EMPLOYEE')")
    public PerformanceOverall getManagerPerformanceOverall(@Argument Integer employeeId, @Argument Integer cycleId) {
        return performanceService.getManagerPerformanceOverall(employeeId, cycleId);
    }

    @QueryMapping(name = "managerPerformanceCategoryRating")
    @PreAuthorize("hasAuthority('SUM') or hasAuthority('EMPLOYEE')")
    public List<PerformanceCategoryRating> getManagerPerformanceCategoryRating(@Argument Integer employeeId, @Argument Integer cycleId) {
        return performanceService.getManagerPerformanceCategoryRating(employeeId, cycleId);
    }

    @QueryMapping(name = "managerPerformanceQuestionRating")
    @PreAuthorize("hasAuthority('SUM') or hasAuthority('EMPLOYEE')")
    public List<PerformanceQuestionRating> getManagerPerformanceQuestionRating(@Argument Integer employeeId, @Argument Integer cycleId) {
        return performanceService.getManagerPerformanceQuestionRating(employeeId, cycleId);
    }

    @QueryMapping(name = "finalPerformanceOverall")
    @PreAuthorize("hasAuthority('SUM') or hasAuthority('EMPLOYEE') or hasAuthority('HR')")
    public PerformanceOverall getFinalPerformanceOverall(@Argument Integer employeeId, @Argument Integer cycleId) {
        return performanceService.getFinalPerformanceOverall(employeeId, cycleId);
    }

    @QueryMapping(name = "finalPerformanceCategoryRating")
    @PreAuthorize("hasAuthority('SUM') or hasAuthority('EMPLOYEE') or hasAuthority('HR')")
    public List<PerformanceCategoryRating> getFinalPerformanceCategoryRating(@Argument Integer employeeId, @Argument Integer cycleId) {
        return performanceService.getFinalPerformanceCategoryRating(employeeId, cycleId);
    }

    @QueryMapping(name = "finalPerformanceQuestionRating")
    @PreAuthorize("hasAuthority('SUM') or hasAuthority('EMPLOYEE') or hasAuthority('HR')")
    public List<PerformanceQuestionRating> getFinalPerformanceQuestionRating(@Argument Integer employeeId, @Argument Integer cycleId) {
        return performanceService.getFinalPerformanceQuestionRating(employeeId, cycleId);
    }

    @QueryMapping(name = "performanceEvaluationList")
    @PreAuthorize("hasAuthority('SUM')")
    public EvaluationPaging getCompetencyEvaluationList(@Argument Integer departmentId, @Argument Integer cycleId,
                                                        @Nullable @Argument String name, @Argument Integer pageNo,
                                                        @Argument Integer pageSize) {
        return performanceService.getCompetencyEvaluationList(departmentId,cycleId,name,pageNo,pageSize);
    }

    @MutationMapping(name = "selfPerformanceEvaluation")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public Boolean createEmployeeEvaluation(@Argument PerformanceEvaluationInput input) {
        return performanceService.createEmployeeEvaluation(input);
    }

    @MutationMapping(name = "managerPerformanceEvaluation")
    @PreAuthorize("hasAuthority('SUM')")
    public Boolean createManagerEvaluation(@Argument PerformanceEvaluationInput input) {
        return performanceService.createManagerEvaluation(input);
    }
    @MutationMapping(name = "finalPerformanceEvaluation")
    @PreAuthorize("hasAuthority('SUM')")
    public Boolean createFinalEvaluation(@Argument PerformanceEvaluationInput input) {
        return performanceService.createFinalEvaluation(input);
    }

    @QueryMapping(name = "comparePerformanceChart")
    @PreAuthorize("hasAuthority('HR')")
    public ChartData getComparePerformanceChart(@Argument List<Integer> employeeIds) {
        return performanceService.getComparePerformanceChart(employeeIds);
    }

















    @QueryMapping(name = "averagePerformanceScore")
    @PreAuthorize("hasAuthority('MANAGER')")
    public Float getAveragePerformanceScore(@Argument Integer cycleId) {
        return performanceService.getAveragePerformanceScore(cycleId);
    }

    @QueryMapping(name = "performanceCyclePeriod")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER')")
    public String performanceCyclePeriod(@Argument Integer cycleId) {
        return performanceService.performanceCyclePeriod(cycleId);
    }





    //Don't have API in graphql yet
    @QueryMapping
    @PreAuthorize("hasAuthority('MANAGER')")
    public Page<PerformanceEvaluationOverall> getPerformanceEvaluations(@Argument Integer cycleId,
                                                                        @Argument int pageNo,
                                                                        @Argument int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return performanceService.getPerformanceEvaluations(cycleId, pageable);
    }






}