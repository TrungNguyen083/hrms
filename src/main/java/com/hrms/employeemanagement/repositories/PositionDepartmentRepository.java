package com.hrms.employeemanagement.repositories;

import com.hrms.employeemanagement.models.PositionDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionDepartmentRepository extends JpaRepository<PositionDepartment, Integer>,
        JpaSpecificationExecutor<PositionDepartment>
{
}
