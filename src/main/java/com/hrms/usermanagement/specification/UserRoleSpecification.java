package com.hrms.usermanagement.specification;

import com.hrms.usermanagement.model.UserRole;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRoleSpecification {

    public static Specification<UserRole> hasUserIds(List<Integer> userIds) {
        return ((root, query, criteriaBuilder) -> root.get("user").get("userId").in(userIds));
    }
}
