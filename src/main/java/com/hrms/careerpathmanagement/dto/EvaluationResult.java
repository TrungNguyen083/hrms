package com.hrms.careerpathmanagement.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvaluationResult {
    private Integer employeeId;
    private String profileImage;
    private String firstName;
    private String lastName;
    private String position;
    private String level;
    private Float competencyRating;
    private Float performanceRating;
    private String potential;
    private String finalStatus;
}
