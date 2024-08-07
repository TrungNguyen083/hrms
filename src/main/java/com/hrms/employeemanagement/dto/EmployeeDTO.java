package com.hrms.employeemanagement.dto;

import com.hrms.employeemanagement.models.EmergencyContact;
import com.hrms.employeemanagement.models.Employee;
import com.hrms.global.models.Skill;

import java.util.List;

public record EmployeeDTO(Employee employee, String imageUrl, List<EmergencyContact> emergencyContacts, List<Skill> skills) {
}
