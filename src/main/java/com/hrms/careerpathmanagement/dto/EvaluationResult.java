package com.hrms.careerpathmanagement.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EvaluationResult {
    private Integer skillId;
    private Integer selfEvaluation;
    private Integer evaluatorEvaluation;
}
