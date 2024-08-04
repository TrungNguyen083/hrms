package com.hrms.usermanagement.repository;

import com.hrms.usermanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User>
{
    Optional<User> findByUsername(@Param("username") String username);
    Boolean existsByUsername(String username);

    @Modifying
    @Query("UPDATE User u SET u.isEnabled = :status WHERE u.userId IN :userIds")
    void updateIsEnabledForUserIds(@Param("status") Boolean status, @Param("userIds") List<Integer> userIds);

    @Modifying
    @Query("UPDATE User u SET u.role = :roleId WHERE u.userId IN :userIds")
    void updateRoleForUserIds(@Param("roleId") Integer roleId, @Param("userIds") List<Integer> userIds);

}

