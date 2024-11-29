package com.hrms.careerpathmanagement.repositories;

import com.hrms.careerpathmanagement.models.EmployeeCareerPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeCareerPathRepository extends JpaRepository<EmployeeCareerPath, Integer>, JpaSpecificationExecutor<EmployeeCareerPath> {
}
