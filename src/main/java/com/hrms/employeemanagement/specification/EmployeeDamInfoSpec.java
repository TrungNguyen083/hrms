package com.hrms.employeemanagement.specification;

import com.hrms.employeemanagement.models.EmployeeDamInfo;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeDamInfoSpec {
    public static Specification<EmployeeDamInfo> hasEmployeeAndType(Integer id, String type) {
        return  (root, query, builder) -> builder.and(
                builder.equal(root.get("employee").get("id"), id),
                builder.equal(root.get("type"), type)
        );
    }

}
