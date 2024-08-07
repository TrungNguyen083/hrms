package com.hrms.performancemanagement.dto;


import com.hrms.global.models.JobLevel;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StackedBarChart {
    private List<JobLevel> labels;
    private List<DatasetDTO> datasets;
}
