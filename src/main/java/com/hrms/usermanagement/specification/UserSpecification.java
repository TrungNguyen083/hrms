package com.hrms.usermanagement.specification;

import com.hrms.usermanagement.model.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserSpecification {
    public Specification<User> hasId(@Nullable Integer id) {
        return id == null ? null : (root, query, cb) -> cb.equal(root.get("id"), id);
    }

    public Specification<User> hasRoleId(@Nullable Integer roleId) {
        return roleId == null ? null : (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("role").get("id"), roleId);
    }

    public Specification<User> hasStatus(@Nullable Boolean status) {
        return status == null ? null : (root, query, cb) -> cb.equal(root.get("isEnabled"), status);
    }

    public Specification<User> hasSearch(@Nullable String search) {
        return search == null ? null : (root, query, cb) -> cb.like(root.get("username"), "%" + search + "%");
    }

    public Specification<User> getUsersSpec(@Nullable String search,
                                            @Nullable Integer roleId,
                                            @Nullable Boolean status) {
        return Specification.where(hasSearch(search))
                .and(hasRoleId(roleId))
                .and(hasStatus(status));
    }
}
