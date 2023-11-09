package com.hrms.careerpathmanagement.services;

import com.hrms.careerpathmanagement.dto.PositionLevelNodeDTO;
import com.hrms.careerpathmanagement.models.PositionLevelPath;
import com.hrms.employeemanagement.models.PositionLevel;

import java.util.List;

public interface CareerPathManagementService {
    List<PositionLevel> getNextPositionLevel(Integer currentPositionLevelId);

    PositionLevelNodeDTO getCareerPath(Integer employeeId);
}