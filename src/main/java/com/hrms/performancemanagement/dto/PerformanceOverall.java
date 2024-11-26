package com.hrms.performancemanagement.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceOverall {
    private String evaluationCycleName;
    private String firstName;
    private String lastName;
    private String profileImage;
    private String position;
    private String level;
    private String rating;
    private Integer potential;
    private String status;
    private Boolean isSubmit;
}
