package com.hrms.global.dto;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BarChartDTO {
    String title;
    List<DataItemDTO> items;
}
