package com.hrms.employeemanagement.dto.pagination;

import com.hrms.employeemanagement.dto.EmployeeRatingDTO;
import com.hrms.global.paging.Pagination;

import java.util.List;

public record EmployeeRatingPagination(List<EmployeeRatingDTO> data, Pagination pagination) {
}
