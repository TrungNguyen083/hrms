package com.hrms.careerpathmanagement.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompetencyGroupRating {
    private Integer id;
    private String competencyGroupName;
    private Integer weight;
    private Float rating;
}
