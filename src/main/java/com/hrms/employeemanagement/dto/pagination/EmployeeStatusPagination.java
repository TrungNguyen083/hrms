package com.hrms.employeemanagement.dto.pagination;

import com.hrms.employeemanagement.dto.EmployeeStatusDTO;
import com.hrms.global.paging.Pagination;

import java.util.List;

public record EmployeeStatusPagination(List<EmployeeStatusDTO> data, Pagination pagination) {
}
