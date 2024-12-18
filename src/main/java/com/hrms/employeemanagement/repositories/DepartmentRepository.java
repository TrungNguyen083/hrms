package com.hrms.employeemanagement.repositories;

import com.hrms.global.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Integer>, JpaSpecificationExecutor<Department> {
    List<Department> findAllByIsEvaluate(Boolean isEvaluate);
    Department findDepartmentBySum_Id(Integer sumId);
}
