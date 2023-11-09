package com.hrms.careerpathmanagement.services;

import com.hrms.careerpathmanagement.dto.*;
import com.hrms.careerpathmanagement.models.CompetencyTimeLine;
import jakarta.annotation.Nullable;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public interface CompetencyService {
    @Scheduled(cron = "0 0 0 * * *")
    void updateIsDoneForOverdueItems();
    List<CompetencyTimeLine> getCompetencyTimeline(Integer competencyCycleId);
    List<DepartmentInCompleteDTO> getDepartmentIncompletePercent(Integer competencyCycleId);
    List<CompanyIncompletedDTO> getCompanyIncompletePercent(Integer competencyCycleId);
    List<AvgCompetencyDTO> getAvgCompetencies(Integer positionId, Integer competencyCycleId);
    SkillSetPagingDTO getHighestSkillSet(@Nullable Integer empId, @Nullable Integer competencyCycleId, int pageNo, int pageSize);
    List<EmployeeSkillMatrixDTO> getEmployeeSkillMatrix(Integer employeeId);
    SkillMatrixOverallDTO getSkillMatrixOverall(Integer employeeId);
    SkillSetPagingDTO getTopKeenSkillSetEmployee(Integer employeeId, int pageNo, int pageSize);
    SkillSetPagingDTO getTopHighestSkillSetTargetEmployee(Integer employeeId, int pageNo, int pageSize);
    CurrentEvaluationDTO getCurrentEvaluation(Integer employeeId);
    List<HistoryEvaluationDTO> getHistoryEvaluations(Integer employeeId);

    SkillSetSummarizationDTO getSkillSummarization(Integer employeeId, Integer cycleId);

    CompanyCompetencyDiffPercentDTO getCompanyCompetencyDiffPercent();

    List<CompetencyChartDTO> getCompetencyChart();

    RadarChartDTO getOverallCompetencyRadarChart(Integer employeeId, Integer cycleId);


    RadarChartDTO getCompetencyRadarChart(List<Integer> competencyCyclesId, Integer departmentId);
}
