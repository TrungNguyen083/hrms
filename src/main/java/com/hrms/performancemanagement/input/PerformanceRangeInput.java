package com.hrms.performancemanagement.input;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceRangeInput {
    private Integer minValue;
    private Integer maxValue;
    private String text;
}
