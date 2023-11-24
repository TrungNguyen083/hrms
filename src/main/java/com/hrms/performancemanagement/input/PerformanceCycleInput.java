package com.hrms.performancemanagement.input;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PerformanceCycleInput {
    private String cycleName;
    private String description;
    private String performanceCycleStartDate;
    private String performanceCycleEndDate;
    private Integer template;
    private Float performanceWeightage;
    private Float goalWeightage;
}
