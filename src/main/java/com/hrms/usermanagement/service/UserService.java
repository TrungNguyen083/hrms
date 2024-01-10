package com.hrms.usermanagement.service;

import com.hrms.global.mapper.HrmsMapper;
import com.hrms.global.paging.Pagination;
import com.hrms.usermanagement.dto.SignupDto;
import com.hrms.usermanagement.dto.UserDto;
import com.hrms.usermanagement.dto.UserDtoPagination;
import com.hrms.usermanagement.model.Role;
import com.hrms.usermanagement.model.User;
import com.hrms.usermanagement.model.UserRole;
import com.hrms.usermanagement.repository.RoleRepository;
import com.hrms.usermanagement.repository.UserRepository;
import com.hrms.usermanagement.repository.UserRoleRepository;
import com.hrms.usermanagement.specification.UserRoleSpecification;
import com.hrms.usermanagement.specification.UserSpecification;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class UserService {

    @jakarta.persistence.PersistenceContext
    jakarta.persistence.EntityManager em;
    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;
    private PasswordEncoder passwordEncoder;
    private UserSpecification userSpecification;
    private HrmsMapper modelMapper;
    private RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository,
                       PasswordEncoder passwordEncoder, UserSpecification userSpecification,
                       HrmsMapper modelMapper, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userSpecification = userSpecification;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
    }

    public UserService() {
    }

    private void checkUserExist(String username) throws Exception {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(username))) {
            throw new Exception("User already exists");
        }
    }

    public UserDtoPagination searchUsers(@Nullable String search,
                                         @Nullable List<Integer> roleIds,
                                         @Nullable Boolean status,
                                         Pageable pageable) {
        Specification<User> spec = userSpecification.getUsersSpec(search, roleIds, status);

        Page<User> users = userRepository.findAll(spec, pageable);

        Pagination pagination = new Pagination(pageable.getPageNumber() + 1, pageable.getPageSize(),
                users.getTotalElements(),
                users.getTotalPages()
        );

        return new UserDtoPagination(users.map(u -> modelMapper.map(u, UserDto.class)), pagination, users.getTotalElements());
    }

    public List<Role> getRoles(Integer userId) {
        Specification<UserRole> spec = (root, query, builder) -> builder.equal(root.get("user").get("userId"), userId);

        return userRoleRepository.findAll(spec).stream().map(UserRole::getRole).toList();
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
    public Boolean createUser(SignupDto signupDto) throws Exception {
        checkUserExist(signupDto.getUsername());

        var user = new User();
        user.setUsername(signupDto.getUsername());
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        user.setIsEnabled(false);
        user.setCreatedAt(Date.valueOf(LocalDate.now()));

        userRepository.save(user);

        return Boolean.TRUE;
    }

    @Transactional
    public Boolean updateUsers(List<Integer> userIds, Boolean status, List<Integer> roleIds) {
        deleteUserRolesNotInRoles(userIds, roleIds);
        List<Pair<Integer, Integer>> userRolePairItems = getUserRolePairItems(userIds, roleIds);
        List<Pair<Integer, Integer>> userRolePairsExisting = userRoleRepository.findAll(UserRoleSpecification.hasUserIds(userIds))
                        .stream().map(userRole -> Pair.of(userRole.getUser().getUserId(), userRole.getRole().getRoleId()))
                        .toList();
        insertUserRolePairs(userRolePairItems, userRolePairsExisting);
        userRepository.updateIsEnabledForUserIds(status, userIds);
        return Boolean.TRUE;
    }

    private void deleteUserRolesNotInRoles(List<Integer> userIds, List<Integer> roleIds) {
        Specification<UserRole> deleteNotInRoles = (root, query, criteriaBuilder) -> {
            Predicate roleIdNotInPredicate = root.get("role").get("roleId").in(roleIds).not();
            Predicate userIn = root.get("user").get("userId").in(userIds);
            return criteriaBuilder.and(roleIdNotInPredicate, userIn);
        };
        userRoleRepository.delete(deleteNotInRoles);
    }

    private List<Pair<Integer, Integer>> getUserRolePairItems(List<Integer> userIds, List<Integer> roleIds) {
        return userIds.stream()
                .flatMap(userId -> roleIds.stream().map(roleId -> Pair.of(userId, roleId)))
                .toList();
    }

    private void insertUserRolePairs(List<Pair<Integer, Integer>> userRolePairItems,
                                    List<Pair<Integer, Integer>> userRolePairsExisting) {
        List<Pair<Integer, Integer>> userRolePairsToInsert = userRolePairItems.stream()
                .filter(userRolePairItem -> !userRolePairsExisting.contains(userRolePairItem))
                .toList();
        userRolePairsToInsert.forEach(
                userRolePair -> {
                    var user = new User();
                    user.setUserId(userRolePair.getFirst());

                    var role = new Role();
                    role.setRoleId(userRolePair.getSecond());

                    var userRole = new UserRole();
                    userRole.setUser(user);
                    userRole.setRole(role);
                    userRoleRepository.save(userRole);
                }
        );
    }

    static Predicate getEqualUserIdsPredicate(List<Integer> userIds, Root<UserRole> root) {
        return root.get("user").get("userId").in(userIds);
    }

//    private void updateUsersStatus(List<Integer> userIds, Boolean status) {
//        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//        CriteriaUpdate<User> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(User.class);
//        CriteriaUpdate<User> update = criteriaUpdate.set("isEnabled", status);
//
//        Root<User> root = update.from(User.class);
//        update.where(root.get("userId").in(userIds));
//
//        em.createQuery(update).executeUpdate();
//    }

}