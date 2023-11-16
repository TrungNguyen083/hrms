package com.hrms.careerpathmanagement.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class PieChartDTO {
    private List<String> labels;
    private List<Float> datasets;
}
