package com.hrms.careerpathmanagement.services;

import com.hrms.careerpathmanagement.dto.*;
import com.hrms.careerpathmanagement.dto.pagination.EmployeeEvaProgressPaging;
import com.hrms.careerpathmanagement.input.CompetencyEvaluationInput;
import com.hrms.careerpathmanagement.input.EvaluationProcessInput;
import com.hrms.employeemanagement.dto.EmployeeStatusDTO;
import com.hrms.employeemanagement.dto.pagination.EmployeeRatingPagination;
import com.hrms.global.dto.*;
import jakarta.annotation.Nullable;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.ParseException;
import java.util.List;

public interface CompetencyService {
    @Scheduled(cron = "0 0 0 * * *")
    void updateIsDoneForOverdueItems();
    MultiBarChartDTO getDepartmentCompleteComp(Integer evaluateCycleId);
    PieChartDTO getCompetencyEvalProgress(Integer evaluateCycleId);
    List<HeatmapItemDTO> getHeatmapCompetency(Integer positionId, Integer evaluateCycleId);
    DataItemPagingDTO getTopSkill(@Nullable Integer departmentId, @Nullable Integer empId,
                                  Integer evaluateCycleId, int pageNo, int pageSize);
    List<EmployeeSkillMatrixDTO> getEmployeeSkillMatrix(Integer employeeId);
    DataItemPagingDTO getTopKeenSkillEmployee(Integer employeeId, int pageNo, int pageSize);
    DataItemPagingDTO getTopSkillTargetEmployee(Integer employeeId, int pageNo, int pageSize, Integer evaluateCycleId);
    CurrentEvaluationDTO getCurrentEvaluation(Integer employeeId);
    List<HistoryEvaluationDTO> getHistoryEvaluations(Integer employeeId);
    BarChartDTO getSkillGap(Integer employeeId, Integer cycleId);
    DiffPercentDTO getCompetencyDiffPercent(Integer departmentId, Integer cycleId);
    BarChartDTO getCompetencyOverviewChart(Integer departmentId, Integer cycleId);
    RadarChartDTO getOverallCompetencyRadarChart(Integer employeeId, Integer evaluateCycleId);
    RadarChartDTO getCompetencyRadarChart(List<Integer> evaluateCycleIds, Integer departmentId);

    EmployeeRatingPagination getCompetencyRating(Integer departmentId, Integer cycleId, Integer pageNo, Integer pageSize);

    PieChartDTO getCompetencyLevelPieChart(Integer employeeId, Integer cycleId);

    MultiBarChartDTO getSumDepartmentCompletePercent(Integer cycleId, Integer departmentId);

    List<EmployeeStatusDTO> getCompetencyEvaluationsStatus(Integer cycleId, Integer departmentId);
    List<EmployeeStatusDTO> getPerformanceEvaluationStatus(Integer cycleId, Integer departmentId);

    List<HeatmapItemDTO> getDepartmentSkillHeatmap(Integer cycleId,
                                                   List<Integer> employeeIds, List<Integer> competencyIds);

    RadarChartDTO getDepartmentCompetencyGap(Integer cycleId, List<Integer> employeeIds);

    String evaluateCyclePeriod(Integer evaluateCycleId);

    List<TimeLine> createCompetencyProcess(EvaluationProcessInput input) throws ParseException;

    EmployeeEvaProgressPaging getTrackEvaluationProgress(Integer evaluateCycleId, Integer pageNo, Integer pageSize);

    List<TreeSimpleData> getEvaluateSkillForm(Integer employeeId);

    List<CompetencyGroupDTO> getCompetencyGroups();

    List<EvaluationResult> getEvaluationResult(Integer employeeId, Integer evaluateCycleId);

    Boolean createSelfCompetencyEvaluation(CompetencyEvaluationInput input);

    Boolean createEvaluatorCompetencyEvaluation(CompetencyEvaluationInput input);

    Boolean createFinalCompetencyEvaluation(CompetencyEvaluationInput input);

    List<CycleOverallDTO> getCyclesOverall();

    PieChartDTO getCompetencyEvaProgressPieChart(Integer cycleId, Integer departmentId);
}
