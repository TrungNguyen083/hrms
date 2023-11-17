package com.hrms.careerpathmanagement.services;

import com.hrms.careerpathmanagement.dto.*;
import com.hrms.careerpathmanagement.models.CompetencyCycle;
import com.hrms.careerpathmanagement.models.CompetencyTimeLine;
import com.hrms.employeemanagement.dto.EmployeeRatingPagination;
import com.hrms.global.dto.*;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public interface CompetencyService {
    @Scheduled(cron = "0 0 0 * * *")
    void updateIsDoneForOverdueItems();
    List<CompetencyTimeLine> getCompetencyTimeline(Integer competencyCycleId);
    MultiBarChartDTO getDepartmentIncompletePercent(Integer competencyCycleId);
    PieChartDTO getCompanyIncompletePercent(Integer competencyCycleId);
    List<HeatmapItemDTO> getHeatmapCompetency(Integer positionId, Integer competencyCycleId);
    DataItemPagingDTO getHighestSkillSet(@Nullable Integer empId, @Nullable Integer competencyCycleId, int pageNo, int pageSize);
    List<EmployeeSkillMatrixDTO> getEmployeeSkillMatrix(Integer employeeId);
    SkillMatrixOverallDTO getSkillMatrixOverall(Integer employeeId);
    DataItemPagingDTO getTopKeenSkillSetEmployee(Integer employeeId, int pageNo, int pageSize);
    DataItemPagingDTO getTopHighestSkillSetTargetEmployee(Integer employeeId, int pageNo, int pageSize);
    List<CurrentEvaluationDTO> getCurrentEvaluation(Integer employeeId);
    List<HistoryEvaluationDTO> getHistoryEvaluations(Integer employeeId);
    SkillSetSummarizationDTO getSkillSummarization(Integer employeeId, Integer cycleId);
    DiffPercentDTO getCompanyCompetencyDiffPercent();
    BarChartDTO getCompetencyChart();
    RadarChartDTO getOverallCompetencyRadarChart(Integer employeeId, Integer cycleId);
    List<TargetPositionLevelDTO> getTargetCareerPath(Integer employeeId);
    RadarChartDTO getCompetencyRadarChart(List<Integer> competencyCyclesId, Integer departmentId);
    List<EvaluationCycleInfoDTO> getEvaluationCycles();

    BarChartDTO getSkillGapBarChart(Integer employeeId, Integer cycleId);

    List<CompetencyCycle> getCompetencyCycles();

    EmployeeRatingPagination getCompetencyRating(Integer cycleId, PageRequest pageable);
}
