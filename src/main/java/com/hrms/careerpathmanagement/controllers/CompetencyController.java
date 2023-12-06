package com.hrms.careerpathmanagement.controllers;

import com.hrms.careerpathmanagement.dto.*;
import com.hrms.careerpathmanagement.input.CompetencyCycleInput;
import com.hrms.careerpathmanagement.input.CompetencyEvaluationInput;
import com.hrms.careerpathmanagement.input.EvaluationProcessInput;
import com.hrms.careerpathmanagement.input.TemplateInput;
import com.hrms.careerpathmanagement.models.*;
import com.hrms.careerpathmanagement.services.CompetencyService;
import com.hrms.employeemanagement.dto.SimpleItemDTO;
import com.hrms.employeemanagement.dto.pagination.EmployeeRatingPagination;
import com.hrms.employeemanagement.dto.pagination.EmployeeStatusPagination;
import com.hrms.global.dto.*;
import com.hrms.performancemanagement.dto.EvaluationCycleDTO;
import jakarta.annotation.Nullable;

import java.text.ParseException;
import java.util.Collections;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
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

    @QueryMapping(name = "topSkillSet")
    public DataItemPagingDTO getTopSkillSet(@Argument @Nullable Integer departmentId,
                                                @Argument @Nullable Integer employeeId,
                                                @Argument @Nullable Integer competencyCycleId,
                                                @Argument int pageNo, @Argument int pageSize) {
        try {
            return competencyService.getTopSkillSet(departmentId, employeeId, competencyCycleId, pageNo, pageSize);
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
        return competencyService.getTopSkillSetTargetEmployee(empId, pageNo, pageSize);
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
    public DiffPercentDTO getCompanyCompetencyDiffPercent(@Argument @Nullable Integer departmentId) {
        return competencyService.getCompanyCompetencyDiffPercent(departmentId);
    }

    @QueryMapping(name = "competencyChart")
    public BarChartDTO getCompetencyChart(@Argument @Nullable Integer departmentId) {
        return competencyService.getCompetencyChart(departmentId);
    }


    /**
     * HR Dashboard - Top Competencies Component
     * @return List Employees (name, profileImg) with their competency rating
     */
    @QueryMapping(name = "topCompetencyRating")
    public EmployeeRatingPagination getTopEmployeeCompetencies(@Argument @Nullable Integer departmentId,
                                                               @Argument Integer cycleId,
                                                               @Argument Integer pageNo,
                                                               @Argument Integer pageSize)
    {
        return competencyService.getCompetencyRating(departmentId, cycleId, pageNo, pageSize);
    }

    /*** Employee Dashboard - Component: Overall competency score ***/
    @QueryMapping(name = "overallCompetencyRadarChart")
    public RadarChartDTO getOverallCompetencyRadarChart(@Argument Integer employeeId, @Argument Integer cycleId) {
        return competencyService.getOverallCompetencyRadarChart(employeeId, cycleId);
    }

    /***
     ********************************************** SUM Dashboard ****************************************
     * Global filter: CycleId
     */
    @QueryMapping(name = "incompletedEvaluationByPosition")
    public MultiBarChartDTO getIncompletedEvaluationByPosition(@Argument Integer cycleId, @Argument Integer departmentId) {
        return competencyService.getSumDepartmentIncompletePercent(cycleId, departmentId);
    }


    //COMPONENT: Competency Evaluation Status
    @QueryMapping(name = "competencyEvaluationStatus")
    public EmployeeStatusPagination getCompetencyEvaluationsStatus(@Argument Integer cycleId,
                                                                   @Argument Integer departmentId,
                                                                   @Argument Integer pageNo,
                                                                   @Argument Integer pageSize)
    {
        return competencyService.getCompetencyEvaluationsStatus(cycleId, departmentId, PageRequest.of(pageNo - 1, pageSize));
    }


    @QueryMapping(name = "skillSets")
    public List<SimpleItemDTO> getSkillSetByPosition(@Argument Integer positionId) {
        return competencyService.getSkillSetByPosition(positionId);
    }

    @QueryMapping(name = "departmentSkillSetHeatMap")
    public List<HeatmapItemDTO> getDepartmentSkillSetHeatMap(@Argument Integer departmentId, @Argument Integer cycleId,
                                                             @Argument List<Integer> employeeIds, @Argument List<Integer> skillSetIds) {
        return competencyService.getDepartmentSkillSetHeatmap(departmentId, cycleId, employeeIds, skillSetIds);
    }

    @QueryMapping(name = "departmentCompetencyGap")
    public RadarChartDTO getDepartmentCompetencyGap(@Argument Integer cycleId, @Argument List<Integer> employeeIds) {
        return competencyService.getDepartmentCompetencyGap(cycleId, employeeIds);
    }

    @QueryMapping(name = "evaluationCycles")
    public List<EvaluationCycleDTO> getEvaluationCycles() {
        return competencyService.getEvaluationCycles();
    }

    @Transactional
    @MutationMapping(name = "createCompetencyCycle")
    public CompetencyCycle createCompetencyCycle(@Argument CompetencyCycleInput input) {
        return competencyService.createCompetencyCycle(input);
    }

    @QueryMapping(name = "competencyCyclePeriod")
    public String competencyCyclePeriod(@Argument Integer cycleId) {
        return competencyService.competencyCyclePeriod(cycleId);
    }

    @Transactional
    @MutationMapping(name = "createCompetencyProcess")
    public List<TimeLine> createCompetencyProcess(@Argument EvaluationProcessInput input) throws ParseException {
        return competencyService.createCompetencyProcess(input);
    }

    @QueryMapping(name = "templates")
    public List<TemplateDTO> getTemplates() {
        return competencyService.getTemplates();
    }

    @MutationMapping(name = "createTemplate")
    public Boolean createTemplate(@Argument TemplateInput input) {
        return competencyService.createTemplate(input);
    }

    @QueryMapping(name = "evaluateSkillSetForm")
    public List<TreeSimpleData> getEvaluateSkillSetForm(@Argument Integer employeeId) {
        return competencyService.getEvaluateSkillSetForm(employeeId);
    }

    @QueryMapping(name = "competencyGroups")
    public List<CompetencyGroupDTO> getCompetencyGroups() {
        return competencyService.getCompetencyGroups();
    }

    @QueryMapping(name = "evaluationResult")
    public List<EvaluationResult> getEvaluationResult(@Argument Integer employeeId, @Argument Integer cycleId) {
        return competencyService.getEvaluationResult(employeeId, cycleId);
    }

    @MutationMapping(name = "createSelfCompetencyEvaluation")
    public Boolean createSelfCompetencyEvaluation(@Argument CompetencyEvaluationInput input) {
        return competencyService.createSelfCompetencyEvaluation(input);
    }

    @MutationMapping(name = "createEvaluatorCompetencyEvaluation")
    public Boolean createEvaluatorCompetencyEvaluation(@Argument CompetencyEvaluationInput input) {
        return competencyService.createEvaluatorCompetencyEvaluation(input);
    }

    @MutationMapping(name = "createFinalCompetencyEvaluation")
    public Boolean createFinalCompetencyEvaluation(@Argument CompetencyEvaluationInput input) {
        return competencyService.createFinalCompetencyEvaluation(input);
    }
}
