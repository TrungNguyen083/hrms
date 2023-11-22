package com.hrms.careerpathmanagement.services;

import com.hrms.careerpathmanagement.dto.*;
import com.hrms.careerpathmanagement.models.CompetencyCycle;
import com.hrms.employeemanagement.dto.pagination.EmployeeRatingPagination;
import com.hrms.employeemanagement.dto.pagination.EmployeeStatusPagination;
import com.hrms.global.dto.*;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public interface CompetencyService {
    @Scheduled(cron = "0 0 0 * * *")
    void updateIsDoneForOverdueItems();
    List<TimeLine> getCompetencyTimeline(Integer competencyCycleId);
    MultiBarChartDTO getDepartmentInCompleteComp(Integer competencyCycleId);
    PieChartDTO getCompetencyEvalProgress(Integer competencyCycleId);
    List<HeatmapItemDTO> getHeatmapCompetency(Integer positionId, Integer competencyCycleId);
    DataItemPagingDTO getHighestSkillSet(@Nullable Integer empId, @Nullable Integer competencyCycleId, int pageNo, int pageSize);
    List<EmployeeSkillMatrixDTO> getEmployeeSkillMatrix(Integer employeeId);
    SkillMatrixOverallDTO getSkillMatrixOverall(Integer employeeId);
    DataItemPagingDTO getTopKeenSkillSetEmployee(Integer employeeId, int pageNo, int pageSize);
    DataItemPagingDTO getTopHighestSkillSetTargetEmployee(Integer employeeId, int pageNo, int pageSize);
    List<CurrentEvaluationDTO> getCurrentEvaluation(Integer employeeId);
    List<HistoryEvaluationDTO> getHistoryEvaluations(Integer employeeId);
    BarChartDTO getSkillSetGap(Integer employeeId, Integer cycleId);
    DiffPercentDTO getCompanyCompetencyDiffPercent();
    BarChartDTO getCompetencyChart();
    RadarChartDTO getOverallCompetencyRadarChart(Integer employeeId, Integer cycleId);
    RadarChartDTO getCompetencyRadarChart(List<Integer> competencyCyclesId, Integer departmentId);

    List<CompetencyCycle> getCompetencyCycles();

    EmployeeRatingPagination getCompetencyRating(Integer cycleId, PageRequest pageable);

    PieChartDTO getCompetencyLevelPieChart(Integer employeeId, Integer cycleId);

    List<String> getSkillSetNamesByPosition(Integer positionId);
  
    MultiBarChartDTO getSumDepartmentIncompletePercent(Integer cycleId, Integer departmentId);

    EmployeeStatusPagination getCompetencyEvaluationsStatus(Integer cycleId, Integer departmentId, Pageable page);
}
