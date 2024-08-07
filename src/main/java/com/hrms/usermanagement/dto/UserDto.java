package com.hrms.usermanagement.dto;

import com.hrms.employeemanagement.models.Employee;
import com.hrms.usermanagement.model.Role;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private Integer userId;
    private String userName;
    private Boolean status;
    private Role role;
    private Date createdAt;
    private Employee employee;
    private String profileImage;
}