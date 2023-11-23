package com.hrms.careerpathmanagement.dto.pagination;

import com.hrms.careerpathmanagement.dto.EmployeeGoalDTO;
import com.hrms.global.paging.Pagination;

import java.util.List;

public record EmployeeGoalPagination(List<EmployeeGoalDTO> data, Pagination pagination) {
}
