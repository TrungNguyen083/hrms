package com.hrms.employeemanagement.dto.pagination;


import com.hrms.employeemanagement.dto.EmployeeDTO;
import com.hrms.global.paging.Pagination;

import java.util.List;


public record EmployeePagingDTO(List<EmployeeDTO> data, Pagination pagination) {
}
