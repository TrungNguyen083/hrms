package com.hrms.careerpathmanagement.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeEvaProgress {
    private Integer employeeId;
    private String name;
    private String image;
    private String selfStatus;
    private String evaluatorStatus;
    private String finalStatus;
}
