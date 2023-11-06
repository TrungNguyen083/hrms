package com.hrms.careerpathmanagement.repositories;

import com.hrms.careerpathmanagement.models.PositionLevelPath;
import com.hrms.employeemanagement.models.PositionLevel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PositionLevelPathRepository extends JpaRepository<PositionLevelPath, Integer>,
        JpaSpecificationExecutor<PositionLevelPath>
{
    List<PositionLevelPath> findAllByCurrentId(Integer currentId);
}
