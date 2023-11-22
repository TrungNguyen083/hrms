package com.hrms.employeemanagement.projection;


public record NameAndStatusOnly(Integer id, String firstName, String lastName, String status) {
}