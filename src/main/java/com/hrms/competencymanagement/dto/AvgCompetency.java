package com.hrms.competencymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvgCompetency {
    private Integer jobLevelId;
    private Integer competencyId;
    private Float average;
}
