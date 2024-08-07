package com.hrms.careerpathmanagement.services;

import com.hrms.careerpathmanagement.dto.*;
import com.hrms.careerpathmanagement.dto.pagination.EmployeeEvaProgressPaging;
import com.hrms.careerpathmanagement.input.EvaluateCycleInput;
import com.hrms.careerpathmanagement.input.CompetencyEvaluationInput;
import com.hrms.careerpathmanagement.input.EvaluationProcessInput;
import com.hrms.careerpathmanagement.input.TemplateInput;
import com.hrms.global.models.EvaluateCycle;
import com.hrms.employeemanagement.dto.SimpleItemDTO;
import com.hrms.employeemanagement.dto.pagination.EmployeeRatingPagination;
import com.hrms.employeemanagement.dto.pagination.EmployeeStatusPagination;
import com.hrms.global.dto.*;
import com.hrms.performancemanagement.dto.EvaluationCycleDTO;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.ParseException;
import java.util.List;

public interface CompetencyService {
    @Scheduled(cron = "0 0 0 * * *")
    void updateIsDoneForOverdueItems();
    List<TimeLine> getEvaluateTimeline(Integer evaluateCycleId);
    MultiBarChartDTO getDepartmentInCompleteComp(Integer evaluateCycleId);
    PieChartDTO getCompetencyEvalProgress(Integer evaluateCycleId);
    List<HeatmapItemDTO> getHeatmapCompetency(Integer positionId, Integer evaluateCycleId);
    DataItemPagingDTO getTopSkill(@Nullable Integer departmentId, @Nullable Integer empId,
                                  @Nullable Integer evaluateCycleId, int pageNo, int pageSize);
    List<EmployeeSkillMatrixDTO> getEmployeeSkillMatrix(Integer employeeId);
    SkillMatrixOverallDTO getSkillMatrixOverall(Integer employeeId);
    DataItemPagingDTO getTopKeenSkillEmployee(Integer employeeId, int pageNo, int pageSize);
    DataItemPagingDTO getTopSkillTargetEmployee(Integer employeeId, int pageNo, int pageSize);
    List<CurrentEvaluationDTO> getCurrentEvaluation(Integer employeeId);
    List<HistoryEvaluationDTO> getHistoryEvaluations(Integer employeeId);
    BarChartDTO getSkillGap(Integer employeeId, Integer cycleId);
    DiffPercentDTO getCompanyCompetencyDiffPercent(Integer departmentId);
    BarChartDTO getCompetencyChart(Integer departmentId);
    RadarChartDTO getOverallCompetencyRadarChart(Integer employeeId, Integer cycleId);
    RadarChartDTO getCompetencyRadarChart(List<Integer> evaluateCycleIds, Integer departmentId);

    List<EvaluateCycle> getEvaluateCycles();

    EmployeeRatingPagination getCompetencyRating(Integer departmentId, Integer cycleId, Integer pageNo, Integer pageSize);

    PieChartDTO getCompetencyLevelPieChart(Integer employeeId, Integer cycleId);

    List<SimpleItemDTO> getPositionLevelSkills(Integer positionId, Integer jobLevelId);
  
    MultiBarChartDTO getSumDepartmentIncompletePercent(Integer cycleId, Integer departmentId);

    EmployeeStatusPagination getCompetencyEvaluationsStatus(Integer cycleId, Integer departmentId, Pageable page);

    List<HeatmapItemDTO> getDepartmentSkillHeatmap(Integer departmentId, Integer evaluateCycleId,
                                                   List<Integer> employeeIds, List<Integer> skillIds);

    RadarChartDTO getDepartmentCompetencyGap(Integer evaluateCycleId, List<Integer> employeeIds);

    List<EvaluationCycleDTO> getEvaluationCycles();

    EvaluateCycle createEvaluateCycle(EvaluateCycleInput input);

    String evaluateCyclePeriod(Integer evaluateCycleId);

    List<TimeLine> createCompetencyProcess(EvaluationProcessInput input) throws ParseException;

    List<TemplateDTO> getTemplates();

    Boolean createTemplate(TemplateInput input);

    EmployeeEvaProgressPaging getTrackEvaluationProgress(Integer evaluateCycleId, Integer pageNo, Integer pageSize);

    List<TreeSimpleData> getEvaluateSkillForm(Integer employeeId);

    List<CompetencyGroupDTO> getCompetencyGroups();

    List<EvaluationResult> getEvaluationResult(Integer employeeId, Integer evaluateCycleId);

    Boolean createSelfCompetencyEvaluation(CompetencyEvaluationInput input);

    Boolean createEvaluatorCompetencyEvaluation(CompetencyEvaluationInput input);

    Boolean createFinalCompetencyEvaluation(CompetencyEvaluationInput input);

}
