package com.hrms.employeemanagement.dto;

import com.hrms.employeemanagement.models.Employee;

public record EmployeeDTO(Employee employee, String imageUrl) {
}
