package com.hrms.performancemanagement.input;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceRangeInput {
    private String text;
    private String description;
    private Float minValue;
    private Float maxValue;
    private Integer ordered;
}
