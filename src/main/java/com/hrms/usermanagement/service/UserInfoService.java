package com.hrms.usermanagement.service;

import com.hrms.usermanagement.dto.AuthRequest;
import com.hrms.usermanagement.model.User;
import com.hrms.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Boolean createUser(AuthRequest authRequest) throws Exception {
        checkUserExist(authRequest.getUsername());

        var user = new User();
        user.setUsername(authRequest.getUsername());
        user.setPassword(encoder.encode(authRequest.getPassword()));
        user.setIsEnabled(false);
        user.setCreatedAt(Date.valueOf(LocalDate.now()));

        repository.save(user);

        return Boolean.TRUE;
    }

    private void checkUserExist(String username) throws Exception {
        //If userId is null, check if username exists for any user
        if (Boolean.TRUE.equals(repository.existsByUsername(username))) {
            throw new Exception("Username already exists");
        }
    }


}