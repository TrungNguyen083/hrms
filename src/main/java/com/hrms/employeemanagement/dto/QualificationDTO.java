package com.hrms.employeemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QualificationDTO {
    private String title;
    private String fileName;
    private String url;
    private String uploadAt;

}
