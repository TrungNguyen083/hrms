package com.hrms.careerpathmanagement.controllers;

import com.hrms.careerpathmanagement.dto.*;
import com.hrms.careerpathmanagement.dto.pagination.EmployeeEvaProgressPaging;
import com.hrms.careerpathmanagement.input.EvaluateCycleInput;
import com.hrms.careerpathmanagement.input.CompetencyEvaluationInput;
import com.hrms.careerpathmanagement.input.EvaluationProcessInput;
import com.hrms.careerpathmanagement.input.TemplateInput;
import com.hrms.careerpathmanagement.services.CompetencyService;
import com.hrms.employeemanagement.dto.SimpleItemDTO;
import com.hrms.employeemanagement.dto.pagination.EmployeeRatingPagination;
import com.hrms.employeemanagement.dto.pagination.EmployeeStatusPagination;
import com.hrms.global.dto.*;
import com.hrms.global.models.EvaluateCycle;
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
import org.springframework.security.access.prepost.PreAuthorize;
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

    @QueryMapping(name = "evaluateTimeLine")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('HR')")
    public List<TimeLine> getEvaluateTimeLine(@Argument Integer evaluateCycleId) {
        try {
            return competencyService.getEvaluateTimeline(evaluateCycleId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @QueryMapping(name = "departmentInCompleteComp")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('HR')")
    public MultiBarChartDTO getDepartmentInCompleteComp(@Argument Integer evaluateCycleId) {
        try {
            return competencyService.getDepartmentInCompleteComp(evaluateCycleId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @QueryMapping(name = "competencyEvalProgress")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('HR')")
    public PieChartDTO getCompetencyEvalProgress(@Argument Integer evaluateCycleId) {
        try {
            return competencyService.getCompetencyEvalProgress(evaluateCycleId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @QueryMapping(name = "avgCompetencyScore")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('HR')")
    public List<HeatmapItemDTO> getHeatmapCompetency(@Argument @Nullable Integer positionId,
                                                     @Argument Integer evaluateCycleId) {
        try {
            return competencyService.getHeatmapCompetency(positionId, evaluateCycleId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @QueryMapping(name = "evaluateCycles")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('HR')")
    public List<EvaluateCycle> getEvaluateCycles() {
        try {
            return competencyService.getEvaluateCycles();
        } catch (Exception e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @QueryMapping(name = "competencyRadarChart")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('HR')")
    public RadarChartDTO getCompetencyRadarChart(@Argument List<Integer> evaluateCycleIds,
                                                 @Argument Integer departmentId) {
        try {
            return competencyService.getCompetencyRadarChart(evaluateCycleIds, departmentId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @QueryMapping(name = "topSkill")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('USER')")
    public DataItemPagingDTO getTopSkill(@Argument @Nullable Integer departmentId,
                                                @Argument @Nullable Integer employeeId,
                                                @Argument @Nullable Integer evaluateCycleId,
                                                @Argument int pageNo, @Argument int pageSize) {
        try {
            return competencyService.getTopSkill(departmentId, employeeId, evaluateCycleId, pageNo, pageSize);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @QueryMapping(name = "topKeenSkillEmployee")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('USER')")
    public DataItemPagingDTO getTopKeenSkillEmployee(@Argument(name = "employeeId") Integer empId,
                                                        @Argument Integer pageNo, @Argument Integer pageSize) {
        return competencyService.getTopKeenSkillEmployee(empId, pageNo, pageSize);
    }

    @QueryMapping(name = "topHighestSkillTargetEmployee")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER')")
    public DataItemPagingDTO getTopHighestSkillTargetEmployee(@Argument(name = "employeeId") Integer empId,
                                                                 @Argument Integer pageNo,
                                                                 @Argument Integer pageSize) {
        return competencyService.getTopSkillTargetEmployee(empId, pageNo, pageSize);
    }

    @QueryMapping(name = "employeeSkillMatrix")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER')")
    public List<EmployeeSkillMatrixDTO> getEmployeeSkillMatrix(@Argument(name = "employeeId") Integer empId) {
        try {
            return competencyService.getEmployeeSkillMatrix(empId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @QueryMapping(name = "skillMatrixOverall")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER')")
    public SkillMatrixOverallDTO getEmpSkillMatrixOverall(@Argument(name = "employeeId") Integer empId) {
        return competencyService.getSkillMatrixOverall(empId);
    }

    @QueryMapping(name = "currentEvaluation")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER')")
    public List<CurrentEvaluationDTO> getCurrentEvaluation(@Argument("employeeId") Integer empId) {
        return competencyService.getCurrentEvaluation(empId);
    }

    @QueryMapping(name = "historyEvaluation")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER')")
    public List<HistoryEvaluationDTO> getHistoryEvaluations(@Argument Integer employeeId) {
        return competencyService.getHistoryEvaluations(employeeId);
    }

    @QueryMapping(name = "skillGapBarChart")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER')")
    public BarChartDTO getSkillGap(@Argument Integer employeeId, @Argument Integer cycleId) {
        return competencyService.getSkillGap(employeeId, cycleId);
    }

    @QueryMapping(name = "competencyLevelPieChart")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER')")
    public PieChartDTO getCompetencyLevelPieChart(@Argument Integer employeeId, @Argument Integer cycleId) {
        return competencyService.getCompetencyLevelPieChart(employeeId, cycleId);
    }

    @QueryMapping(name = "companyCompetencyDiffPercent")
    @PreAuthorize("hasAuthority('MANAGER')")
    public DiffPercentDTO getCompanyCompetencyDiffPercent(@Argument @Nullable Integer departmentId) {
        return competencyService.getCompanyCompetencyDiffPercent(departmentId);
    }

    @QueryMapping(name = "competencyChart")
    @PreAuthorize("hasAuthority('MANAGER')")
    public BarChartDTO getCompetencyChart(@Argument @Nullable Integer departmentId) {
        return competencyService.getCompetencyChart(departmentId);
    }


    /**
     * HR Dashboard - Top Competencies Component
     * @return List Employees (name, profileImg) with their competency rating
     */
    @QueryMapping(name = "topCompetencyRating")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('MANAGER')")
    public EmployeeRatingPagination getTopEmployeeCompetencies(@Argument @Nullable Integer departmentId,
                                                               @Argument Integer cycleId,
                                                               @Argument Integer pageNo,
                                                               @Argument Integer pageSize)
    {
        return competencyService.getCompetencyRating(departmentId, cycleId, pageNo, pageSize);
    }

    /*** Employee Dashboard - Component: Overall competency score ***/
    @QueryMapping(name = "overallCompetencyRadarChart")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER')")
    public RadarChartDTO getOverallCompetencyRadarChart(@Argument Integer employeeId, @Argument Integer cycleId) {
        return competencyService.getOverallCompetencyRadarChart(employeeId, cycleId);
    }

    /***
     ********************************************** SUM Dashboard ****************************************
     * Global filter: CycleId
     */
    @QueryMapping(name = "incompletedEvaluationByPosition")
    @PreAuthorize("hasAuthority('MANAGER')")
    public MultiBarChartDTO getIncompletedEvaluationByPosition(@Argument Integer cycleId, @Argument Integer departmentId) {
        return competencyService.getSumDepartmentIncompletePercent(cycleId, departmentId);
    }


    //COMPONENT: Competency Evaluation Status
    @QueryMapping(name = "competencyEvaluationStatus")
    @PreAuthorize("hasAuthority('MANAGER')")
    public EmployeeStatusPagination getCompetencyEvaluationsStatus(@Argument Integer cycleId,
                                                                   @Argument Integer departmentId,
                                                                   @Argument Integer pageNo,
                                                                   @Argument Integer pageSize)
    {
        return competencyService.getCompetencyEvaluationsStatus(cycleId, departmentId, PageRequest.of(pageNo - 1, pageSize));
    }


    @QueryMapping(name = "positionLevelSkills")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER')")
    public List<SimpleItemDTO> getPositionLevelSkills(@Argument Integer positionId, @Argument Integer jobLevelId) {
        return competencyService.getPositionLevelSkills(positionId, jobLevelId);
    }

    @QueryMapping(name = "departmentSkillHeatMap")
    @PreAuthorize("hasAuthority('MANAGER')")
    public List<HeatmapItemDTO> getDepartmentSkillHeatMap(@Argument Integer departmentId, @Argument Integer evaluateCycleId,
                                                             @Argument List<Integer> employeeIds, @Argument List<Integer> skillIds) {
        return competencyService.getDepartmentSkillHeatmap(departmentId, evaluateCycleId, employeeIds, skillIds);
    }

    @QueryMapping(name = "departmentCompetencyGap")
    @PreAuthorize("hasAuthority('MANAGER')")
    public RadarChartDTO getDepartmentCompetencyGap(@Argument Integer evaluateCycleId, @Argument List<Integer> employeeIds) {
        return competencyService.getDepartmentCompetencyGap(evaluateCycleId, employeeIds);
    }

    @QueryMapping(name = "evaluationCycles")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER')")
    public List<EvaluationCycleDTO> getEvaluationCycles() {
        return competencyService.getEvaluationCycles();
    }

    @Transactional
    @MutationMapping(name = "createEvaluateCycle")
    @PreAuthorize("hasAuthority('MANAGER')")
    public EvaluateCycle createEvaluateCycle(@Argument EvaluateCycleInput input) {
        return competencyService.createEvaluateCycle(input);
    }

    @QueryMapping(name = "evaluateCyclePeriod")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('USER')")
    public String evaluateCyclePeriod(@Argument Integer evaluateCycleId) {
        return competencyService.evaluateCyclePeriod(evaluateCycleId);
    }

    @Transactional
    @MutationMapping(name = "createCompetencyProcess")
    @PreAuthorize("hasAuthority('MANAGER')")
    public List<TimeLine> createCompetencyProcess(@Argument EvaluationProcessInput input) throws ParseException {
        return competencyService.createCompetencyProcess(input);
    }

    @QueryMapping(name = "templates")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER')")
    public List<TemplateDTO> getTemplates() {
        return competencyService.getTemplates();
    }

    @MutationMapping(name = "createTemplate")
    @PreAuthorize("hasAuthority('MANAGER')")
    public Boolean createTemplate(@Argument TemplateInput input) {
        return competencyService.createTemplate(input);
    }

    @QueryMapping(name = "trackEvaluationProgress")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER')")
    public EmployeeEvaProgressPaging getTrackEvaluationProgress(@Argument Integer evaluateCycleId,
                                                                @Argument @Nullable Integer pageNo,
                                                                @Argument @Nullable Integer pageSize) {
        return competencyService.getTrackEvaluationProgress(evaluateCycleId, pageNo, pageSize);
    }


    @QueryMapping(name = "evaluateSkillForm")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER')")
    public List<TreeSimpleData> getEvaluateSkillForm(@Argument Integer employeeId) {
        return competencyService.getEvaluateSkillForm(employeeId);
    }

    @QueryMapping(name = "competencyGroups")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER')")
    public List<CompetencyGroupDTO> getCompetencyGroups() {
        return competencyService.getCompetencyGroups();
    }

    @QueryMapping(name = "evaluationResult")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER')")
    public List<EvaluationResult> getEvaluationResult(@Argument Integer employeeId, @Argument Integer evaluateCycleId) {
        return competencyService.getEvaluationResult(employeeId, evaluateCycleId);
    }

    @MutationMapping(name = "createSelfCompetencyEvaluation")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER')")
    public Boolean createSelfCompetencyEvaluation(@Argument CompetencyEvaluationInput input) {
        return competencyService.createSelfCompetencyEvaluation(input);
    }

    @MutationMapping(name = "createEvaluatorCompetencyEvaluation")
    @PreAuthorize("hasAuthority('MANAGER')")
    public Boolean createEvaluatorCompetencyEvaluation(@Argument CompetencyEvaluationInput input) {
        return competencyService.createEvaluatorCompetencyEvaluation(input);
    }

    @MutationMapping(name = "createFinalCompetencyEvaluation")
    @PreAuthorize("hasAuthority('MANAGER')")
    public Boolean createFinalCompetencyEvaluation(@Argument CompetencyEvaluationInput input) {
        return competencyService.createFinalCompetencyEvaluation(input);
    }
}
