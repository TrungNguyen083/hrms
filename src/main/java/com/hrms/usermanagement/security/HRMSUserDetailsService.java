package com.hrms.usermanagement.security;

import com.hrms.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class HRMSUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        var userName = user.getUsername();
        var password = user.getPassword();
        var roles = user.getRoles();
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.stream().forEach(role -> {
                    authorities.add(new SimpleGrantedAuthority(role.getName().toUpperCase()));
                });
        return new org.springframework.security.core.userdetails.User(userName, password, authorities);
    }
}
