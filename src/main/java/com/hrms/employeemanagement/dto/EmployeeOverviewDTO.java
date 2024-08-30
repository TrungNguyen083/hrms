package com.hrms.employeemanagement.dto;

import java.util.List;

public record EmployeeOverviewDTO(Integer id, String firstName, String lastName,
                                  String profileImgUri,
                                  String position, String level,
                                  String address,
                                  List<String> skills,
                                  List<String> qualifications)
{
}
