package com.hrms.careerpathmanagement.repositories;

import com.hrms.careerpathmanagement.models.PositionLevelPath;
import com.hrms.employeemanagement.models.PositionLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NextPositionLevelRepository extends JpaRepository<PositionLevelPath, Integer> {
    PositionLevel findFirstByCurrentId(int currentId);
}
