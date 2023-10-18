package com.hrms.employeemanagement.services;

import com.hrms.employeemanagement.models.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface EmployeeService {
	List<Employee> findAll(Specification<Employee> spec);
	List<Employee> findAll(Specification<Employee> spec, Sort sort);
	Page<Employee> findAll(Specification<Employee> spec, Pageable pageable);
	Employee saveEmployee(Employee employee);
	List<Employee> getNewEmployeeOfMonth();
	Page<Employee> getAllByFilter(List<Integer> ids, List<Integer> currentContacts, Boolean status, String name, Pageable pageable);
	Float getCurrentHeadcounts();
	Employee findById(Integer id);
}
