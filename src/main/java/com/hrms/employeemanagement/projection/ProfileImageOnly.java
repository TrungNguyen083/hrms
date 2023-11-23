package com.hrms.employeemanagement.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileImageOnly {
    private Integer employeeId;
    private String url;

    public ProfileImageOnly(Integer employeeId, String url) {
        this.employeeId = employeeId;
        this.url = url;
    }
}