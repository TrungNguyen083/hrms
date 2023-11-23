package com.hrms.careerpathmanagement.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeGoalDTO {
    Integer id;

    Integer employeeId;
    String firstName;
    String lastName;
    String profileImgUrl;

    String title;
    String description;
    Float progress;
}
