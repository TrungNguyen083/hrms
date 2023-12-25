package com.hrms.usermanagement.repository;

import com.hrms.usermanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User>
{
    User findByUsername(@Param("username") String username);
    Boolean existsByUsername(String username);

    @Query("UPDATE User u SET u.isEnabled = :status WHERE u.userId IN (:ids)")
    void updateIsEnabledByUserIds(Boolean status, List<Integer> ids);
}