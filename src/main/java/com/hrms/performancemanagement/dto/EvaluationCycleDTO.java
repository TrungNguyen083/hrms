package com.hrms.performancemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationCycleDTO {
    private Integer cycleId;
    private String cycleName;
    private String status;
    private String period;
    private String type;
}
