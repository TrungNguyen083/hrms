package com.hrms.careerpathmanagement.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CareerSpecification {
    public <T> Specification<T> hasPositionId(Integer positionId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("position").get("id"), positionId);
    }

    public <T> Specification<T> hasJobLevelId(Integer jobLevelId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("jobLevel").get("id"), jobLevelId);
    }

    public <T> Specification<T> hasDepartmentId(Integer departmentId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("department").get("id"), departmentId);
    }
}
