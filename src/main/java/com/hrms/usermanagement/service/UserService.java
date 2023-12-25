package com.hrms.usermanagement.service;

import com.hrms.global.mapper.HrmsMapper;
import com.hrms.global.paging.Pagination;
import com.hrms.usermanagement.dto.SignupDto;
import com.hrms.usermanagement.dto.UserDto;
import com.hrms.usermanagement.dto.UserDtoPagination;
import com.hrms.usermanagement.model.Role;
import com.hrms.usermanagement.model.User;
import com.hrms.usermanagement.model.UserRole;
import com.hrms.usermanagement.repository.UserRepository;
import com.hrms.usermanagement.repository.UserRoleRepository;
import com.hrms.usermanagement.specification.UserSpecification;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    @PersistenceContext
    private EntityManager em;
    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;
    private PasswordEncoder passwordEncoder;
    private UserSpecification userSpecification;
    private HrmsMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository,
                       PasswordEncoder passwordEncoder, UserSpecification userSpecification,
                       HrmsMapper modelMapper, EntityManager em) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userSpecification = userSpecification;
        this.modelMapper = modelMapper;
        this.em = em;
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

    public UserDto getUser(Integer userId) throws Exception {
        return modelMapper.map(userRepository
                .findById(userId)
                .orElseThrow(() -> new Exception("User Not Exist")), UserDto.class);
    }

    @Transactional
    public User createUser(SignupDto signupDto) throws Exception {
        checkUserExist(signupDto.getUsername());

        var user = new User();
        user.setUsername(signupDto.getUsername());
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        user.setIsEnabled(false);
        user.setCreatedAt(Date.valueOf(LocalDate.now()));

        return userRepository.save(user);
    }

    @Transactional(readOnly = false)
    public Boolean updateUsers(List<Integer> userIds, Boolean status, List<Integer> roleIds) {
        deleteUserRolesNotInRoles(userIds, roleIds);
        List<Pair<Integer, Integer>> userRolePairItems = getUserRolePairItems(userIds, roleIds);
        insertUserRolePairs(userRolePairItems);
        updateUsersStatus(userIds, status);
        return true;
    }

    @Transactional(readOnly = false)
    protected void deleteUserRolesNotInRoles(List<Integer> userIds, List<Integer> roleIds) {
        Specification<UserRole> deleteNotInRoles = (root, query, criteriaBuilder) -> {
            Predicate roleIdNotInPredicate = root.get("role").get("roleId").in(roleIds).not();
            return criteriaBuilder.and(getEqualUserIdsPredicate(userIds, root), roleIdNotInPredicate);
        };
        userRoleRepository.delete(deleteNotInRoles);
    }

    private List<Pair<Integer, Integer>> getUserRolePairItems(List<Integer> userIds, List<Integer> roleIds) {
        return userIds.stream()
                .flatMap(userId -> roleIds.stream().map(roleId -> Pair.of(userId, roleId)))
                .toList();
    }

    @Transactional(readOnly = false)
    protected void insertUserRolePairs(List<Pair<Integer, Integer>> userRolePairItems) {
        userRolePairItems.stream()
                .filter(pair -> userRolePairItems.stream().noneMatch(existingPair -> existingPair.equals(pair)))
                .forEach(pair -> userRoleRepository.addRoleIdUserId(pair.getFirst(), pair.getSecond()));
    }

    static Predicate getEqualUserIdsPredicate(List<Integer> userIds, Root<UserRole> root) {
        return root.get("user").get("userId").in(userIds);
    }

    @Transactional(readOnly = false)
    protected void updateUsersStatus(List<Integer> userIds, boolean status) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaUpdate<User> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(User.class);
        CriteriaUpdate<User> update = criteriaUpdate.set("isEnabled", status);

        Root<User> root = update.from(User.class);
        update.where(root.get("userId").in(userIds));

        em.createQuery(update).executeUpdate();
    }

}