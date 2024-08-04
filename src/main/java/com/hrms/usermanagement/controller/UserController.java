package com.hrms.usermanagement.controller;

import com.hrms.global.mapper.HrmsMapper;
import com.hrms.usermanagement.dto.UserDto;
import com.hrms.usermanagement.dto.UserDtoPagination;
import com.hrms.usermanagement.model.Role;
import com.hrms.usermanagement.service.UserService;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    private final HrmsMapper userMapper;



    @Autowired
    public UserController(UserService userService, HrmsMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    // ---------------------------------- BUSINESS FLOW --------------------------

    @QueryMapping
    public UserDto user(@Argument Integer userId) throws Exception {
        var user = userService.getUser(userId);
        return userMapper.map(user, UserDto.class);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserDtoPagination users(@Nullable @Argument String search,
                                   @Nullable @Argument Integer roleId,
                                   @Nullable @Argument Boolean status,
                                   @Argument int pageNo,
                                   @Argument int pageSize)
    {
        var pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("createdAt").descending());
        return userService.searchUsers(search, roleId, status, pageable);
    }



    @MutationMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Boolean updateUsers(@Argument Integer userId,
                               @Argument Boolean status,
                               @Argument Integer roleId)
    {
        return userService.updateUsers(userId, status, roleId);
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Boolean assignUser(@Argument Integer userId,
                               @Argument Integer employeeId)
    {
        return userService.assignUser(userId, employeeId);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Role> roles() {
        return userService.getRoles();
    }

    @MutationMapping(name = "updateUsernamePassword")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Boolean updateUsernamePassword(@Argument Integer userId,
                                          @Argument String username,
                                          @Argument String password) throws Exception {
        return userService.updateUsernamePassword(userId, username, password);
    }
}
