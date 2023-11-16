package com.hrms.employeemanagement.dto;

import com.hrms.global.paging.Pagination;

import java.util.List;

public record EmployeeRatingPagination(List<EmployeeRatingDTO> data, Pagination pagination) {
}
