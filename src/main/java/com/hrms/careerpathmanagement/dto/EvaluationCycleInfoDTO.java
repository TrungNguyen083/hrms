package com.hrms.careerpathmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationCycleInfoDTO {
    private String cycleName;
    private String cycleStatus;
    private String cyclePeriod;
    private String cycleType;
    private CycleEvaluationProgressDTO cycleProgress;
}
