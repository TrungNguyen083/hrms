package com.hrms.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MultiBarChartDTO {
    private List<String> labels;
    private List<MultiBarChartDataDTO> datasets;
}
