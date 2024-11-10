package com.hrms.careerpathmanagement.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeEvaProgress {
    private Integer employeeId;
    private String profileImage;
    private String firstName;
    private String lastName;
    private String position;
    private String level;
    private String employeeStatus;
    private String evaluatorStatus;
    private String finalStatus;
}
