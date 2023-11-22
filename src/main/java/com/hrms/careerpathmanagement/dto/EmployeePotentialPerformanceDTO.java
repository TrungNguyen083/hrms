package com.hrms.careerpathmanagement.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePotentialPerformanceDTO {
    String fullName;
    String profileImgUri;
    Float potential;
    Float performance;
}
