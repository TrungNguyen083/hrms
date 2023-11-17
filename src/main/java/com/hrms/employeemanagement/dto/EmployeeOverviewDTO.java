package com.hrms.employeemanagement.dto;

import java.util.List;

public record EmployeeOverviewDTO(Integer id, String firstName, String lastName,
                                  String profileImgUrl,
                                  String position, String level,
                                  List<String> skillsetName, List<String> interests,
                                  List<String> certificates)
{
}
