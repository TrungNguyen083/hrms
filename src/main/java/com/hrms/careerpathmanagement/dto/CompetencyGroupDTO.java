package com.hrms.careerpathmanagement.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompetencyGroupDTO {
    private Integer id;
    private String competencyGroupName;
    private Float weight;
    private List<Integer> competencyIds;
}
