package com.hrms.usermanagement.service;

import com.hrms.usermanagement.dto.SignupDto;
import com.hrms.usermanagement.model.User;
import com.hrms.usermanagement.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userDetail = repository.findByUsername(username);

        // Converting userDetail to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }

    @Transactional
    public Boolean createUser(SignupDto signupDto) throws Exception {
        checkUserExist(signupDto.getUsername(), null);

        var user = new User();
        user.setUsername(signupDto.getUsername());
        user.setPassword(encoder.encode(signupDto.getPassword()));
        user.setIsEnabled(false);
        user.setCreatedAt(Date.valueOf(LocalDate.now()));

        repository.save(user);

        return Boolean.TRUE;
    }

    private void checkUserExist(String username, Integer userId) throws Exception {
        //If userId not null, check if username exists for other users
        if (userId != null) {
            Specification<User> spec = (root, query, builder) -> {
                Predicate usernamePredicate = builder.equal(root.get("username"), username);
                Predicate userIdPredicate = builder.notEqual(root.get("userId"), userId);
                return builder.and(usernamePredicate, userIdPredicate);
            };
            if (repository.exists(spec)) {
                throw new Exception("Username already exists");
            }
        } else {
            //If userId is null, check if username exists for any user
            if (Boolean.TRUE.equals(repository.existsByUsername(username))) {
                throw new Exception("Username already exists");
            }
        }
    }


}