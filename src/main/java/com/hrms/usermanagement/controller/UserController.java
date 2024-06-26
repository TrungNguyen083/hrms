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
//    @PreAuthorize("hasAuthority('ADMIN')")
    public UserDtoPagination users(@Nullable @Argument String search,
                                   @Nullable @Argument List<Integer> roles,
                                   @Nullable @Argument Boolean status,
                                   @Argument int pageNo,
                                   @Argument int pageSize)
    {
        var pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("createdAt").descending());
        var users = userService.searchUsers(search, roles, status, pageable);
        users.data().stream().forEach(user -> user.setRoles(userService.getRoles(user.getUserId())));
        return users;
    }



    @MutationMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Boolean updateUsers(@Argument List<Integer> ids,
                               @Argument Boolean status,
                               @Argument List<Integer> roles)
    {
        return userService.updateUsers(ids, status, roles);
    }

    @QueryMapping
//    @PreAuthorize("hasAuthority('ADMIN')")
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
