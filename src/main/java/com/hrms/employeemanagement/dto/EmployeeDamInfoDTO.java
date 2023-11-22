package com.hrms.employeemanagement.dto;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDamInfoDTO {
    int employeeId;
    String extension;
    String fileUri;
    String logoUri;
    Date createdAt;
}
