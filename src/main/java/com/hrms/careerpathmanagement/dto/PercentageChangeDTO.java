package com.hrms.careerpathmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PercentageChangeDTO {
    private Integer data;
    private Float diffPercent;
    private Boolean isIncreased;
}
