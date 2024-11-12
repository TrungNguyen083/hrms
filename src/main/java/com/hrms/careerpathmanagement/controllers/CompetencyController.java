package com.hrms.careerpathmanagement.controllers;

import com.hrms.careerpathmanagement.dto.*;
import com.hrms.careerpathmanagement.input.CompetencyEvaluationInput;
import com.hrms.careerpathmanagement.input.EvaluationProcessInput;
import com.hrms.careerpathmanagement.services.CompetencyService;
import com.hrms.employeemanagement.dto.EmployeeStatusDTO;
import com.hrms.employeemanagement.dto.pagination.EmployeeRatingPagination;
import com.hrms.global.dto.*;
import com.hrms.global.models.CompetencyGroup;
import jakarta.annotation.Nullable;

import java.text.ParseException;
import java.util.Collections;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    /***
     ********************************************** HR Dashboard ****************************************
     */

    @QueryMapping(name = "departmentCompleteComp")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('HR')")
    public MultiBarChartDTO getDepartmentCompleteComp(@Argument Integer evaluateCycleId) {
        try {
            return competencyService.getDepartmentCompleteComp(evaluateCycleId);
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
    @PreAuthorize("hasAuthority('SUM') or hasAuthority('HR') or hasAuthority('EMPLOYEE')")
    public DataItemPagingDTO getTopSkill(@Argument @Nullable Integer departmentId,
                                                @Argument @Nullable Integer employeeId,
                                                @Argument Integer evaluateCycleId,
                                                @Argument int pageNo, @Argument int pageSize) {
        try {
            return competencyService.getTopSkill(departmentId, employeeId, evaluateCycleId, pageNo, pageSize);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
    @QueryMapping(name = "topCompetencyRating")
    @PreAuthorize("hasAuthority('HR') or hasAuthority('SUM')")
    public EmployeeRatingPagination getTopEmployeeCompetencies(@Argument @Nullable Integer departmentId,
                                                               @Argument Integer cycleId,
                                                               @Argument Integer pageNo,
                                                               @Argument Integer pageSize)
    {
        return competencyService.getCompetencyRating(departmentId, cycleId, pageNo, pageSize);
    }

    @QueryMapping(name = "competencyOverviewChart")
    @PreAuthorize("hasAuthority('SUM') or hasAuthority('HR')")
    public BarChartDTO getCompetencyOverviewChart(@Argument @Nullable Integer departmentId,
                                          @Argument Integer cycleId)
    {
        return competencyService.getCompetencyOverviewChart(departmentId, cycleId);
    }

    @QueryMapping(name = "competencyDiffPercent")
    @PreAuthorize("hasAuthority('SUM') or hasAuthority('HR')")
    public DiffPercentDTO getCompetencyDiffPercent(@Argument @Nullable Integer departmentId,
                                                          @Argument Integer cycleId) {
        return competencyService.getCompetencyDiffPercent(departmentId, cycleId);
    }

    /***
     ********************************************** Employee Dashboard ****************************************
     */

    @QueryMapping(name = "topKeenSkillEmployee")
    @PreAuthorize("hasAuthority('PM') or hasAuthority('EMPLOYEE')")
    public DataItemPagingDTO getTopKeenSkillEmployee(@Argument(name = "employeeId") Integer empId,
                                                        @Argument Integer pageNo, @Argument Integer pageSize) {
        return competencyService.getTopKeenSkillEmployee(empId, pageNo, pageSize);
    }

    @QueryMapping(name = "topHighestSkillTargetEmployee")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public DataItemPagingDTO getTopHighestSkillTargetEmployee(@Argument(name = "employeeId") Integer empId,
                                                                 @Argument Integer pageNo,
                                                                 @Argument Integer pageSize, @Argument Integer evaluateCycleId) {
        return competencyService.getTopSkillTargetEmployee(empId, pageNo, pageSize, evaluateCycleId);
    }

    @QueryMapping(name = "employeeSkillMatrix")
    @PreAuthorize("hasAuthority('HR') or hasAuthority('EMPLOYEE')")
    public List<EmployeeSkillMatrixDTO> getEmployeeSkillMatrix(@Argument(name = "employeeId") Integer empId) {
        try {
            return competencyService.getEmployeeSkillMatrix(empId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @QueryMapping(name = "currentEvaluation")
    @PreAuthorize("hasAuthority('PM') or hasAuthority('EMPLOYEE')")
    public CurrentEvaluationDTO getCurrentEvaluation(@Argument("employeeId") Integer empId) {
        return competencyService.getCurrentEvaluation(empId);
    }

    @QueryMapping(name = "historyEvaluation")
    @PreAuthorize("hasAuthority('PM') or hasAuthority('EMPLOYEE')")
    public List<HistoryEvaluationDTO> getHistoryEvaluations(@Argument Integer employeeId) {
        return competencyService.getHistoryEvaluations(employeeId);
    }

    @QueryMapping(name = "skillGapBarChart")
    @PreAuthorize("hasAuthority('PM') or hasAuthority('EMPLOYEE')")
    public BarChartDTO getSkillGap(@Argument Integer employeeId, @Argument Integer cycleId) {
        return competencyService.getSkillGap(employeeId, cycleId);
    }

    @QueryMapping(name = "competencyPieChart")
    @PreAuthorize("hasAuthority('PM') or hasAuthority('EMPLOYEE')")
    public PieChartDTO getCompetencyLevelPieChart(@Argument Integer employeeId, @Argument Integer cycleId) {
        return competencyService.getCompetencyLevelPieChart(employeeId, cycleId);
    }

    @QueryMapping(name = "overallCompetencyRadarChart")
    @PreAuthorize("hasAuthority('PM') or hasAuthority('EMPLOYEE')")
    public RadarChartDTO getOverallCompetencyRadarChart(@Argument Integer employeeId, @Argument Integer evaluateCycleId) {
        return competencyService.getOverallCompetencyRadarChart(employeeId, evaluateCycleId);
    }






    /***
     ********************************************** SUM Dashboard ****************************************
     * Global filter: CycleId
     */
    @QueryMapping(name = "completedEvaluationByPosition")
    @PreAuthorize("hasAuthority('SUM')")
    public MultiBarChartDTO getCompletedEvaluationByPosition(@Argument Integer cycleId, @Argument Integer departmentId) {
        return competencyService.getSumDepartmentCompletePercent(cycleId, departmentId);
    }

    @QueryMapping(name = "competencyEvaluationProgressPieChart")
    @PreAuthorize("hasAuthority('SUM')")
    public PieChartDTO getCompetencyEvaProgressPieChart(@Argument Integer cycleId, @Argument Integer departmentId) {
        return competencyService.getCompetencyEvaProgressPieChart(cycleId, departmentId);
    }


    @QueryMapping(name = "competencyEvaluationStatus")
    @PreAuthorize("hasAuthority('SUM')")
    public List<EmployeeStatusDTO> getCompetencyEvaluationsStatus(@Argument Integer cycleId, @Argument Integer departmentId)
    {
        return competencyService.getCompetencyEvaluationsStatus(cycleId, departmentId);
    }

    @QueryMapping(name = "performanceEvaluationStatus")
    @PreAuthorize("hasAuthority('SUM')")
    public List<EmployeeStatusDTO> getPerformanceEvaluationStatus(@Argument Integer cycleId, @Argument Integer departmentId)
    {
        return competencyService.getPerformanceEvaluationStatus(cycleId, departmentId);
    }

    @QueryMapping(name = "departmentSkillHeatMap")
    @PreAuthorize("hasAuthority('SUM')")
    public List<HeatmapItemDTO> getDepartmentSkillHeatMap(@Argument Integer cycleId, @Argument List<Integer> employeeIds,
                                                          @Argument List<Integer> competencyIds) {
        return competencyService.getDepartmentSkillHeatmap(cycleId, employeeIds, competencyIds);
    }

    @QueryMapping(name = "departmentCompetencyGap")
    @PreAuthorize("hasAuthority('SUM')")
    public RadarChartDTO getDepartmentCompetencyGap(@Argument Integer cycleId, @Argument List<Integer> employeeIds) {
        return competencyService.getDepartmentCompetencyGap(cycleId, employeeIds);
    }

    /***
     ********************************************** HR - Cycle, Template, Rating Control ****************************************
     */

    @QueryMapping(name = "cyclesOverall")
    @PreAuthorize("hasAuthority('HR')")
    public List<CycleOverallDTO> getCyclesOverall() {
        return competencyService.getCyclesOverall();
    }


    /***
     ********************************************** Competency Framework ****************************************
     */
    @QueryMapping(name = "competencyMatrixTree")
    @PreAuthorize("hasAuthority('HR')")
    public List<CompetencyMatrixTree> getCompetencyMatrixTree() {
        return competencyService.getCompetencyMatrixTree();
    }

    @QueryMapping(name = "competencyBaseLine")
    @PreAuthorize("hasAuthority('HR')")
    public List<HeatmapItemDTO> getCompetencyBaseLine(@Argument Integer positionId) {
        return competencyService.getCompetencyBaseLine(positionId);
    }

    @QueryMapping(name = "competencyGroups")
    @PreAuthorize("hasAuthority('HR')")
    public List<CompetencyGroup> getCompetencyGroups() {
        return competencyService.getCompetencyGroups();
    }


    /***
     ********************************************** Competency Evaluation ****************************************
     */

    @QueryMapping(name = "competencyOverall")
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('SUM') or hasAuthority('PM') or hasAuthority('HR')")
    public CompetencyOverallDTO getCompetencyOverall(@Argument Integer employeeId, @Argument Integer cycleId) {
        return competencyService.getCompetencyOverall(employeeId, cycleId);
    }

    @QueryMapping(name = "competencyEvaluationForm")
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('SUM') or hasAuthority('PM') or hasAuthority('HR')")
    public List<CompetencyForm> getCompetencyEvaluationForm(@Argument Integer employeeId, @Argument Integer cycleId) {
        return competencyService.getCompetencyEvaluationForm(employeeId, cycleId);
    }

    @QueryMapping(name = "competencyGroupRating")
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('SUM') or hasAuthority('PM') or hasAuthority('HR')")
    public List<CompetencyGroupRating> getCompetencyGroupRating(@Argument Integer employeeId, @Argument Integer cycleId) {
        return competencyService.getCompetencyGroupRating(employeeId, cycleId);
    }

    @QueryMapping(name = "managerCompetencyOverall")
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('SUM') or hasAuthority('PM') or hasAuthority('HR')")
    public CompetencyOverallDTO getManagerCompetencyOverall(@Argument Integer employeeId, @Argument Integer cycleId) {
        return competencyService.getManagerCompetencyOverall(employeeId, cycleId);
    }

    @QueryMapping(name = "managerCompetencyEvaluationForm")
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('SUM') or hasAuthority('PM') or hasAuthority('HR')")
    public List<CompetencyForm> getManagerCompetencyEvaluationForm(@Argument Integer employeeId, @Argument Integer cycleId) {
        return competencyService.getManagerCompetencyEvaluationForm(employeeId, cycleId);
    }

    @QueryMapping(name = "managerCompetencyGroupRating")
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('SUM') or hasAuthority('PM') or hasAuthority('HR')")
    public List<CompetencyGroupRating> getManagerCompetencyGroupRating(@Argument Integer employeeId, @Argument Integer cycleId) {
        return competencyService.getManagerCompetencyGroupRating(employeeId, cycleId);
    }

    @QueryMapping(name = "finalCompetencyOverall")
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('SUM') or hasAuthority('PM') or hasAuthority('HR')")
    public CompetencyOverallDTO getFinalCompetencyOverall(@Argument Integer employeeId, @Argument Integer cycleId) {
        return competencyService.getFinalCompetencyOverall(employeeId, cycleId);
    }

    @QueryMapping(name = "finalCompetencyEvaluationForm")
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('SUM') or hasAuthority('PM') or hasAuthority('HR')")
    public List<CompetencyForm> getFinalCompetencyEvaluationForm(@Argument Integer employeeId, @Argument Integer cycleId) {
        return competencyService.getFinalCompetencyEvaluationForm(employeeId, cycleId);
    }

    @QueryMapping(name = "finalCompetencyGroupRating")
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('SUM') or hasAuthority('PM') or hasAuthority('HR')")
    public List<CompetencyGroupRating> getFinalCompetencyGroupRating(@Argument Integer employeeId, @Argument Integer cycleId) {
        return competencyService.getFinalCompetencyGroupRating(employeeId, cycleId);
    }

    @MutationMapping(name = "activeNewEvaluation")
    @PreAuthorize("hasAuthority('HR')")
    public Boolean initEmployeesEvaluation(@Argument Integer cycleId) {
        return competencyService.activeNewEvaluation(cycleId);
    }

    @MutationMapping(name = "selfCompetencyEvaluation")
    @PreAuthorize("hasAuthority('EMPLOYEE')or hasAuthority('PM')")
    public Boolean createSelfEvaluation(@Argument CompetencyEvaluationInput input) {
        return competencyService.createSelfEvaluation(input);
    }

    @MutationMapping(name = "managerCompetencyEvaluation")
    @PreAuthorize("hasAuthority('SUM')")
    public Boolean createManagerEvaluation(@Argument CompetencyEvaluationInput input) {
        return competencyService.createManagerEvaluation(input);
    }

    @MutationMapping(name = "finalCompetencyEvaluation")
    @PreAuthorize("hasAuthority('SUM')")
    public Boolean createFinalEvaluation(@Argument CompetencyEvaluationInput input) {
        return competencyService.createFinalEvaluation(input);
    }

    @QueryMapping(name = "employeeFeedback")
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('SUM') or hasAuthority('PM') or hasAuthority('HR')")
    public List<EmployeeFeedback> getEmployeeFeedback(@Argument Integer employeeId, @Argument Integer cycleId) {
        return competencyService.getEmployeeFeedback(employeeId, cycleId);
    }

    @QueryMapping(name = "evaluationTitle")
    @PreAuthorize("hasAuthority('SUM')")
    public EvaluationTitle getEvaluationTitle(@Argument Integer cycleId) {
        return competencyService.getEvaluationTitle(cycleId);
    }

    @QueryMapping(name = "competencyEvaluationList")
    @PreAuthorize("hasAuthority('SUM')")
    public EvaluationPaging getCompetencyEvaluationList(@Argument Integer departmentId, @Argument Integer cycleId,
                                                                  @Nullable @Argument String name, @Argument Integer pageNo,
                                                                  @Argument Integer pageSize) {
        return competencyService.getCompetencyEvaluationList(departmentId,cycleId,name,pageNo,pageSize);
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

    @QueryMapping(name = "evaluationResult")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER')")
    public List<EvaluationResult> getEvaluationResult(@Argument Integer employeeId, @Argument Integer evaluateCycleId) {
        return competencyService.getEvaluationResult(employeeId, evaluateCycleId);
    }


    //
}
