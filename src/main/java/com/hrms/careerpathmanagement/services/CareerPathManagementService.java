package com.hrms.careerpathmanagement.services;

import com.hrms.employeemanagement.models.PositionLevel;

import java.util.List;

public interface CareerPathManagementService {
    List<PositionLevel> getNextPositionLevels(Integer currentPositionLevelId);

}