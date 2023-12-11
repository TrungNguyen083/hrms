package com.hrms.careerpathmanagement.dto.pagination;

import com.hrms.careerpathmanagement.dto.EmployeeEvaProgress;
import com.hrms.global.paging.Pagination;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeEvaProgressPaging {
    private List<EmployeeEvaProgress> data;
    private Pagination pagination;
}
