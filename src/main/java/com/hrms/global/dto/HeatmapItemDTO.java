package com.hrms.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HeatmapItemDTO {
    private String verticalColumnName;
    private String horizontalColumnName;
    private Float score;
}
