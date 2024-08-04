package com.hrms.careerpathmanagement.repositories;

import com.hrms.careerpathmanagement.models.PositionLevelPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PositionLevelPathRepository extends JpaRepository<PositionLevelPath, Integer>,
        JpaSpecificationExecutor<PositionLevelPath>
{

}
