package com.hrms.careerpathmanagement.dto.pagination;

import com.hrms.careerpathmanagement.dto.EvaluationPromotion;
import com.hrms.global.paging.Pagination;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionPaging {
    List<EvaluationPromotion> data;
    Pagination pagination;
}
