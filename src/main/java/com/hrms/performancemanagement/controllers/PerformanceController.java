package com.hrms.performancemanagement.controllers;

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
import com.hrms.performancemanagement.input.PerformanceCycleInput;
import com.hrms.performancemanagement.input.PerformanceRangeInput;
import com.hrms.performancemanagement.input.ProficiencyLevelInput;
import com.hrms.performancemanagement.model.PerformanceEvaluation;
import com.hrms.performancemanagement.model.PerformanceRange;
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

import java.text.ParseException;
import java.util.List;


@RestController
public class PerformanceController {

    private final PerformanceService performanceService;

    @Autowired
    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @QueryMapping(name = "departmentInCompletePerform")
    @PreAuthorize("hasAuthority('MANAGER')")
    public MultiBarChartDTO getDepartmentInCompletePerform(@Argument Integer performanceCycleId) {
        return performanceService.getDepartmentInCompletePerform(performanceCycleId);
    }

    @QueryMapping(name = "performanceEvalProgress")
    @PreAuthorize("hasAuthority('MANAGER')")
    public PieChartDTO getPerformanceEvalProgress(@Argument Integer performanceCycleId) {
        return performanceService.getPerformanceEvalProgress(performanceCycleId);
    }

    @QueryMapping(name = "averagePerformanceScore")
    @PreAuthorize("hasAuthority('MANAGER')")
    public Float getAveragePerformanceScore(@Argument Integer cycleId) {
        return performanceService.getAveragePerformanceScore(cycleId);
    }

    /**
     * HR Dashboard - Component: Performance by Job Level
     *
     * @return a StackedBarChart, including 5 bars: Unsatisfactory, Needs Improvement, Meets Expectations, Exceeds Expectations, Outstanding
     */

    @QueryMapping(name = "performanceByJobLevel")
    @PreAuthorize("hasAuthority('MANAGER')")
    public StackedBarChart getPerformanceByJobLevel(@Argument Integer positionId,
                                                    @Argument Integer cycleId) {
        return performanceService.getPerformanceByJobLevel(positionId, cycleId);
    }

    /**
     * HR Dashboard - Component: Employee Performance & Potential
     *
     * @return Descartes coordinate, x-axis: performance, y-axis: potential
     */
    @QueryMapping(name = "employeesPotentialPerformance")
    @PreAuthorize("hasAuthority('MANAGER')")
    public List<EmployeePotentialPerformanceDTO> getPotentialAndPerformance(@Argument @Nullable Integer departmentId,
                                                                            @Argument Integer cycleId) {
        return performanceService.getPotentialAndPerformance(departmentId, cycleId);
    }


    /**
     * HR Dashboard - Component: Top Performers
     *
     * @return List Employees (name, profileImg) with their performance rating
     */

    @QueryMapping(name = "topPerformers")
    @PreAuthorize("hasAuthority('MANAGER')")
    public EmployeeRatingPagination getPerformanceRating(@Argument @Nullable Integer departmentId,
                                                         @Argument Integer cycleId,
                                                         @Argument Integer pageNo,
                                                         @Argument Integer pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "finalAssessment");
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return performanceService.getPerformanceRating(departmentId, cycleId, pageable);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('MANAGER')")
    public Page<PerformanceEvaluation> getPerformanceEvaluations(@Argument Integer cycleId,
                                                                 @Argument int pageNo,
                                                                 @Argument int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return performanceService.getPerformanceEvaluations(cycleId, pageable);
    }

    /***
     * Employee Dashboard - Component: Performance Rating Scores
     * @return BarChart
     */
    @QueryMapping(name = "employeePerformanceRatingScore")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER')")
    public DataItemPagingDTO getEmployeePerformanceRatingScore(@Argument Integer employeeId,
                                                               @Argument int pageNo,
                                                               @Argument int pageSize) {
        return performanceService.getEmployeePerformanceRatingScore(employeeId, pageNo, pageSize);
    }

    @QueryMapping(name = "performanceTimeLine")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER')")
    public List<TimeLine> getPerformanceTimeLine(@Argument Integer performanceCycleId) {
        return performanceService.getPerformanceTimeLine(performanceCycleId);
    }

    /***
     * SUM Dashboard - Component : Employees Potential Performance
     */
    @QueryMapping(name = "potentialAndPerformanceByPosition")
    @PreAuthorize("hasAuthority('MANAGER')")
    public List<EmployeePotentialPerformanceDTO> getPotentialAndPerformanceByPosition(@Argument Integer departmentId,
                                                                                      @Argument Integer cycleId,
                                                                                      @Argument Integer positionId) {
        return performanceService.getPotentialAndPerformanceByPosition(departmentId, cycleId, positionId);
    }

    @QueryMapping(name = "performanceEvaluationOverview")
    @PreAuthorize("hasAuthority('MANAGER')")
    public DiffPercentDTO getPerformanceEvaOverview(@Argument Integer cycleId,
                                                 @Argument Integer departmentId)
    {
        return performanceService.getPerformanceOverview(cycleId, departmentId);
    }

    @QueryMapping(name = "performanceRatingScheme")
    @PreAuthorize("hasAuthority('MANAGER')")
    public BarChartDTO getPerformanceRatingScheme(@Argument @Nullable Integer departmentId,
                                                  @Argument Integer cycleId)
    {
        return performanceService.getPerformanceRatingScheme(cycleId, departmentId);
    }
  
    @MutationMapping(name = "createPerformanceCycle")
    @PreAuthorize("hasAuthority('MANAGER')")
    public EvaluateCycle createPerformanceCycle(@Argument PerformanceCycleInput input) {
        return performanceService.createPerformanceCycle(input);
    }

    @MutationMapping(name = "updateProficiencyLevel")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ProficiencyLevel updateProficiencyLevel(@Argument Integer id, @Argument ProficiencyLevelInput input)
    {
        return performanceService.updateProficiencyLevel(id, input);
    }

    @MutationMapping(name = "updatePerformanceRange")
    @PreAuthorize("hasAuthority('MANAGER')")
    public PerformanceRange updatePerformanceRage(@Argument Integer id, @Argument PerformanceRangeInput input) {
        return performanceService.updatePerformanceRange(id, input);
    }

    @QueryMapping(name = "performanceCyclePeriod")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER')")
    public String performanceCyclePeriod(@Argument Integer cycleId) {
        return performanceService.performanceCyclePeriod(cycleId);
    }

    @MutationMapping(name = "createPerformanceProcess")
    @PreAuthorize("hasAuthority('MANAGER')")
    public List<TimeLine> createPerformanceProcess(@Argument EvaluationProcessInput input) throws ParseException {
        return performanceService.createPerformanceProcess(input);
    }
}