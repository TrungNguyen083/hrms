package com.hrms.careerpathmanagement.dto;

import com.hrms.global.dto.PieChartDTO;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CycleOverallDTO {
    private String name;
    private String status;
    private String startDate;
    private String dueDate;
    private PieChartDTO completedEvaluate;
    private PieChartDTO competencyOverall;
    private PieChartDTO performanceOverall;
}
