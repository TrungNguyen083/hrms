package com.hrms.usermanagement.dto;

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
    private Boolean isEnable;
    private List<Role> roles;
    private Date createdAt;
}