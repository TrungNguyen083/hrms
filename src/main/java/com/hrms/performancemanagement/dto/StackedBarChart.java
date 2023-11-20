package com.hrms.performancemanagement.dto;


import com.hrms.employeemanagement.models.JobLevel;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceByJobLevalChartDTO {
    private List<JobLevel> labels;
    private List<DatasetDTO> datasets;
}
