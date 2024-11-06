package com.hrms.careerpathmanagement.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompetencyOverallDTO {
    private Integer employeeId;
    private String evaluationCycleName;
    private String firstName;
    private String lastName;
    private String profileImage;
    private String position;
    private String level;
    private Float rating;
    private Boolean isSubmit;
    private String status;
}
