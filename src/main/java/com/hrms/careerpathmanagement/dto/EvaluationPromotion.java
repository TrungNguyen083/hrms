package com.hrms.careerpathmanagement.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvaluationPromotion {
    private Integer employeeId;
    private String profileImage;
    private String firstName;
    private String lastName;
    private String currentPositionLevel;
    private String promotePositionLevel;
    private String status;
}
