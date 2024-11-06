package com.hrms.careerpathmanagement.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompetencyRating {
    private Integer id;
    private String competencyName;
    private String comment;
    private Float rating;
}
