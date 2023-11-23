package com.hrms.careerpathmanagement.dto.pagination;

import com.hrms.careerpathmanagement.dto.GoalDTO;
import com.hrms.careerpathmanagement.projection.GoalProjection;
import com.hrms.global.paging.Pagination;

import java.util.List;

public record GoalPagination(List<GoalProjection> data, Pagination pagination) {
}
