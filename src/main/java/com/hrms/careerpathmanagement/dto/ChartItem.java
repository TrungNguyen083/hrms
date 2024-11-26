package com.hrms.careerpathmanagement.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChartItem {
    private String label;
    private List<Float> dataset;
}
