package com.hrms.careerpathmanagement.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChartData {
    private List<String> labels;
    private List<ChartItem> datasets;
}
