package com.hrms.performancemanagement.input;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceRangeInput {
    private Float minValue;
    private Float maxValue;
    private String text;
}
