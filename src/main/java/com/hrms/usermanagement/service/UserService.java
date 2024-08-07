package com.hrms.usermanagement.service;

import com.hrms.employeemanagement.models.Employee;
import com.hrms.employeemanagement.projection.ProfileImageOnly;
import com.hrms.employeemanagement.repositories.EmployeeDamInfoRepository;
import com.hrms.employeemanagement.repositories.EmployeeRepository;
import com.hrms.global.GlobalSpec;
import com.hrms.global.mapper.HrmsMapper;
import com.hrms.global.paging.Pagination;
import com.hrms.usermanagement.dto.UserDto;
import com.hrms.usermanagement.dto.UserDtoPagination;
import com.hrms.usermanagement.model.Role;
import com.hrms.usermanagement.model.User;
import com.hrms.usermanagement.repository.RoleRepository;
import com.hrms.usermanagement.repository.UserRepository;
import com.hrms.usermanagement.specification.UserSpecification;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserSpecification userSpecification;
    @Autowired
    private HrmsMapper modelMapper;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeDamInfoRepository employeeDamInfoRepository;

    static String PROFILE_IMAGE = "PROFILE_IMAGE";

    private void checkUserExist(String username, Integer userId) throws Exception {
        //If userId not null, check if username exists for other users
        if (userId != null) {
            Specification<User> spec = (root, query, builder) -> {
                Predicate usernamePredicate = builder.equal(root.get("username"), username);
                Predicate userIdPredicate = builder.notEqual(root.get("userId"), userId);
                return builder.and(usernamePredicate, userIdPredicate);
            };
            if (userRepository.exists(spec)) {
                throw new Exception("Username already exists");
            }
        } else {
            //If userId is null, check if username exists for any user
            if (Boolean.TRUE.equals(userRepository.existsByUsername(username))) {
                throw new Exception("Username already exists");
            }
        }
    }

    public UserDtoPagination searchUsers(@Nullable String search,
                                         @Nullable Integer roleId,
                                         @Nullable Boolean status,
                                         Pageable pageable) {
        Specification<User> spec = userSpecification.getUsersSpec(search, roleId, status);

        Page<User> users = userRepository.findAll(spec, pageable);

        List<ProfileImageOnly> images = employeeDamInfoRepository.findByEmployeeIdsSetAndFileType(
                users.stream()
                        .map(User::getEmployee) // Get the employee
                        .filter(Objects::nonNull) // Filter out null employees
                        .map(Employee::getId) // Map to employee IDs
                        .toList(),
                PROFILE_IMAGE
        );

        Pagination pagination = new Pagination(pageable.getPageNumber() + 1, pageable.getPageSize(),
                users.getTotalElements(),
                users.getTotalPages()
        );

        Page<UserDto> userDtos = users.map(u -> modelMapper.map(u, UserDto.class));

        userDtos.getContent().stream()
                .filter(userDto -> Objects.nonNull(userDto.getEmployee()))
                .forEach(i -> images.stream()
                        .filter(j -> j.getEmployeeId().equals(i.getEmployee().getId()))
                        .findFirst()
                        .ifPresent(j -> i.setProfileImage(j.getUrl()))
                );

        return new UserDtoPagination(userDtos, pagination, users.getTotalElements());
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public UserDto getUser(Integer userId) throws Exception {
        return modelMapper.map(userRepository
                .findById(userId)
                .orElseThrow(() -> new Exception("User Not Exist")), UserDto.class);
    }

    @Transactional
    public Boolean updateUsers(Integer userId, Boolean status, Integer roleId) {
        User u = userRepository.findById(userId).orElseThrow();
        Employee e = employeeRepository.findById(u.getEmployee().getId()).orElseThrow();
        if (!status) {
            e.setLeftDate(new Date(System.currentTimeMillis()));
            e.setStatus(false);
        } else {
            e.setJoinedDate(new Date(System.currentTimeMillis()));
            e.setStatus(true);
        }
        u.setIsEnabled(status);
        u.setRole(new Role(roleId));
        userRepository.save(u);
        employeeRepository.save(e);
        return Boolean.TRUE;
    }

    @Transactional
    public Boolean updateUsernamePassword(Integer userId, String username, String password) throws Exception {
        User u = userRepository.findById(userId).orElseThrow();
        checkUserExist(username, u.getUserId());
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(password));
        userRepository.save(u);
        return Boolean.TRUE;
    }

    @Transactional
    public Boolean assignUser(Integer userId, Integer employeeId) {
        User u = userRepository.findById(userId).orElseThrow();
        Employee e = employeeRepository.findById(employeeId).orElseThrow();
        u.setEmployee(e);
        e.setEmail(u.getUsername());
        userRepository.save(u);
        employeeRepository.save(e);
        return Boolean.TRUE;
    }
}