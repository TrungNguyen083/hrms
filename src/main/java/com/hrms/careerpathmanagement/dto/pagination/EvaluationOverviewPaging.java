package com.hrms.careerpathmanagement.dto.pagination;

import com.hrms.careerpathmanagement.dto.EvaluationResult;
import com.hrms.global.paging.Pagination;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvaluationOverviewPaging {
    List<EvaluationResult> data;
    Pagination pagination;
}
