package com.hrms.careerpathmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CycleEvaluationProgressDTO {
    private Float completedPercentage;
    private Float selfEvalPercentage;
    private Float managerEvalPercentage;
}
