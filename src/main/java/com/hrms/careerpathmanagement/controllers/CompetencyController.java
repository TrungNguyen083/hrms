package com.hrms.careerpathmanagement.controllers;

import com.hrms.careerpathmanagement.dto.*;
import com.hrms.careerpathmanagement.models.*;
import com.hrms.careerpathmanagement.services.CompetencyService;
import com.hrms.employeemanagement.dto.EmployeeRatingPagination;
import com.hrms.global.dto.*;
import jakarta.annotation.Nullable;

import java.util.Collections;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class CompetencyController {
    private final CompetencyService competencyService;

    @Autowired
    public CompetencyController(CompetencyService competencyService) {
        this.competencyService = competencyService;
    }

    @QueryMapping(name = "competencyTimeLine")
    public List<TimeLine> getCompetencyTimeLine(@Argument Integer competencyCycleId) {
        try {
            return competencyService.getCompetencyTimeline(competencyCycleId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @QueryMapping(name = "departmentInCompleteComp")
    public MultiBarChartDTO getDepartmentInCompleteComp(@Argument Integer competencyCycleId) {
        try {
            return competencyService.getDepartmentInCompleteComp(competencyCycleId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @QueryMapping(name = "competencyEvalProgress")
    public PieChartDTO getCompetencyEvalProgress(@Argument Integer competencyCycleId) {
        try {
            return competencyService.getCompetencyEvalProgress(competencyCycleId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @QueryMapping(name = "avgCompetencyScore")
    public List<HeatmapItemDTO> getHeatmapCompetency(@Argument @Nullable Integer positionId,
                                                     @Argument Integer competencyCycleId) {
        try {
            return competencyService.getHeatmapCompetency(positionId, competencyCycleId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @QueryMapping(name = "competencyCycles")
    public List<CompetencyCycle> getCompetencyCycles() {
        try {
            return competencyService.getCompetencyCycles();
        } catch (Exception e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @QueryMapping(name = "competencyRadarChart")
    public RadarChartDTO getCompetencyRadarChart(@Argument List<Integer> competencyCyclesId,
                                                 @Argument Integer departmentId) {
        try {
            return competencyService.getCompetencyRadarChart(competencyCyclesId, departmentId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @QueryMapping(name = "topHighestSkillSet")
    public DataItemPagingDTO getTopHighestSkill(@Argument @Nullable Integer employeeId,
                                                @Argument @Nullable Integer competencyCycleId,
                                                @Argument int pageNo, @Argument int pageSize) {
        try {
            return competencyService.getHighestSkillSet(employeeId, competencyCycleId, pageNo, pageSize);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @QueryMapping(name = "topKeenSkillSetEmployee")
    public DataItemPagingDTO getTopKeenSkillSetEmployee(@Argument(name = "employeeId") Integer empId,
                                                        @Argument Integer pageNo, @Argument Integer pageSize) {
        return competencyService.getTopKeenSkillSetEmployee(empId, pageNo, pageSize);
    }

    @QueryMapping(name = "topHighestSkillSetTargetEmployee")
    public DataItemPagingDTO getTopHighestSkillSetTargetEmployee(@Argument(name = "employeeId") Integer empId,
                                                                 @Argument Integer pageNo,
                                                                 @Argument Integer pageSize) {
        return competencyService.getTopHighestSkillSetTargetEmployee(empId, pageNo, pageSize);
    }

    @QueryMapping(name = "employeeSkillMatrix")
    public List<EmployeeSkillMatrixDTO> getEmployeeSkillMatrix(@Argument(name = "employeeId") Integer empId) {
        try {
            return competencyService.getEmployeeSkillMatrix(empId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @QueryMapping(name = "skillMatrixOverall")
    public SkillMatrixOverallDTO getEmpSkillMatrixOverall(@Argument(name = "employeeId") Integer empId) {
        return competencyService.getSkillMatrixOverall(empId);
    }

    @QueryMapping(name = "currentEvaluation")
    public List<CurrentEvaluationDTO> getCurrentEvaluation(@Argument("employeeId") Integer empId) {
        return competencyService.getCurrentEvaluation(empId);
    }

    @QueryMapping(name = "historyEvaluation")
    public List<HistoryEvaluationDTO> getHistoryEvaluations(@Argument Integer employeeId) {
        return competencyService.getHistoryEvaluations(employeeId);
    }

    @QueryMapping(name = "skillGapBarChart")
    public BarChartDTO getSkillSetGap(@Argument Integer employeeId, @Argument Integer cycleId) {
        return competencyService.getSkillSetGap(employeeId, cycleId);
    }

    @QueryMapping(name = "competencyLevelPieChart")
    public PieChartDTO getCompetencyLevelPieChart(@Argument Integer employeeId, @Argument Integer cycleId) {
        return competencyService.getCompetencyLevelPieChart(employeeId, cycleId);
    }

    @QueryMapping(name = "companyCompetencyDiffPercent")
    public DiffPercentDTO getCompanyCompetencyDiffPercent() {
        return competencyService.getCompanyCompetencyDiffPercent();
    }

    @QueryMapping(name = "competencyChart")
    public BarChartDTO getCompetencyChart() {
        return competencyService.getCompetencyChart();
    }


    /**
     * HR Dashboard - Top Competencies Component
     * @return List Employees (name, profileImg) with their competency rating
     */
    @QueryMapping(name = "topCompetencyRating")
    public EmployeeRatingPagination getTopEmployeeCompetencies(@Argument Integer cycleId,
                                                               @Argument Integer pageNo,
                                                               @Argument Integer pageSize)
    {

        return competencyService.getCompetencyRating(cycleId, PageRequest.of(pageNo, pageSize));
    }

    /*** Employee Dashboard - Component: Overall competency score ***/
    @QueryMapping(name = "overallCompetencyRadarChart")
    public RadarChartDTO getOverallCompetencyRadarChart(@Argument Integer employeeId, @Argument Integer cycleId) {
        return competencyService.getOverallCompetencyRadarChart(employeeId, cycleId);
    }
}
