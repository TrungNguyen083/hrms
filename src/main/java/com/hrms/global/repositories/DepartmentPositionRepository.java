package com.hrms.global.repositories;

import com.hrms.global.models.DepartmentPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentPositionRepository extends JpaRepository<DepartmentPosition, Integer>, JpaSpecificationExecutor<DepartmentPosition> {
}
