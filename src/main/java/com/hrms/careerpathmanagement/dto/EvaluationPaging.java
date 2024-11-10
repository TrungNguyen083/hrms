package com.hrms.careerpathmanagement.dto;

import com.hrms.global.paging.Pagination;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvaluationPaging {
    List<EmployeeEvaProgress> data;
    Pagination pagination;
}
