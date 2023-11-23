package com.hrms.employeemanagement.repositories;

import com.hrms.employeemanagement.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>, JpaSpecificationExecutor<Employee>{
    <T> Collection<T> findAllByDepartmentId(Integer departmentId, Class<T> type);
    <T> Collection<T> findAllByIdIn(List<Integer> ids, Class<T> type);
}