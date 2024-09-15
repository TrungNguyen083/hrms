package com.hrms.global.services;

import com.hrms.careerpathmanagement.dto.TimeLine;
import com.hrms.employeemanagement.dto.SimpleItemDTO;
import com.hrms.global.models.*;
import com.hrms.usermanagement.model.Role;

import java.util.List;

public interface CompanyCoreService {
    List<ProficiencyLevel> getProficiencyLevels();

    List<Department> getDepartments();

    Long getNumberOfDepartments();

    List<JobLevel> getJobLevels();

    List<Position> getPositions();

    List<Role> getRoles();

    List<EvaluateCycle> getEvaluateCycles();

    List<TimeLine> getEvaluateTimeline(Integer evaluateCycleId);

    List<SimpleItemDTO> getPositionLevelSkills(Integer positionId, Integer jobLevelId);
}
