package com.hrms.employeemanagement.repositories;

import com.hrms.employeemanagement.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>, JpaSpecificationExecutor<Employee>{
    <T> Collection<T> findAllByIdIn(List<Integer> ids, Class<T> type);
}