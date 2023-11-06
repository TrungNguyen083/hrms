package com.hrms.careerpathmanagement.repositories;

import com.hrms.careerpathmanagement.models.EmployeeCareerPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface EmployeeCareerPathRepository extends JpaRepository<EmployeeCareerPath, Integer>,
        JpaSpecificationExecutor<EmployeeCareerPath>
{
    EmployeeCareerPath findByEmployeeId(int employeeId);
    List<EmployeeCareerPath> findAllByEmployeeId(int employeeId);

}
