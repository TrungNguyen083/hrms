package com.hrms.careerpathmanagement.dto;

import com.hrms.employeemanagement.models.Employee;
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
