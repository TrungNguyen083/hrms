package com.hrms.careerpathmanagement.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePotentialPerformanceDTO {
    Integer employeeId;
    String fullName;
    String profileImgUri;
    Integer potential;
    Float performance;
}
