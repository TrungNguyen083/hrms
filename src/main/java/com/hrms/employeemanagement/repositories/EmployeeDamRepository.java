package com.hrms.employeemanagement.repositories;

import com.hrms.employeemanagement.models.EmployeeDamInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmployeeDamRepository extends JpaRepository<EmployeeDamInfo, Integer>, JpaSpecificationExecutor<EmployeeDamInfo> {
}
