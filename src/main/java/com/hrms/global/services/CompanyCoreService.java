package com.hrms.global.services;

import com.hrms.careerpathmanagement.dto.TimeLine;
import com.hrms.global.input.CompetencyGroupInput;
import com.hrms.global.input.CompetencyInput;
import com.hrms.global.input.EvaluateCycleInput;
import com.hrms.employeemanagement.dto.SimpleItemDTO;
import com.hrms.global.models.*;
import com.hrms.performancemanagement.input.PerformanceRangeInput;
import com.hrms.careerpathmanagement.input.ProficiencyLevelInput;
import com.hrms.performancemanagement.model.PerformanceRange;
import com.hrms.usermanagement.model.Role;

import java.util.List;

public interface CompanyCoreService {
    List<ProficiencyLevel> getProficiencyLevels();
    List<PerformanceRange> getPerformanceRanges();

    List<Department> getDepartments();

    Long getNumberOfDepartments();

    List<JobLevel> getJobLevels();

    List<Position> getPositions();

    List<Role> getRoles();

    List<EvaluateCycle> getEvaluateCycles();

    List<TimeLine> getEvaluateTimeline(Integer evaluateCycleId);

    List<SimpleItemDTO> getPositionLevelSkills(Integer positionId, Integer jobLevelId);

    Boolean createEvaluationCycle(EvaluateCycleInput input);

    Boolean updateProficiencyLevel(Integer id, ProficiencyLevelInput input);

    Boolean updatePerformanceRange(Integer id, PerformanceRangeInput input);

    Boolean createProficiencyLevel(ProficiencyLevelInput input);

    Boolean createPerformanceRange(PerformanceRangeInput input);

    Boolean deleteProficiencyLevel(Integer id);

    Boolean deletePerformanceRange(Integer id);

    List<Competency> getCompetencies();

    Boolean createCompetencyGroup(CompetencyGroupInput input);

    Boolean updateCompetencyGroup(Integer id, CompetencyGroupInput input);

    Boolean deleteCompetencyGroup(Integer id);

    Boolean createCompetency(CompetencyInput input);

    Boolean updateCompetency(Integer id, CompetencyInput input);

    Boolean deleteCompetency(Integer id);
}
