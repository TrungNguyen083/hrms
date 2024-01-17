package com.hrms.usermanagement.service;

import com.hrms.usermanagement.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserInfoDetails implements UserDetails {

    private final String name;
    private final String password;
    private final List<SimpleGrantedAuthority> authorities;

    public UserInfoDetails(User user) {
        name = user.getUsername();
        password = user.getPassword();
        var roles = user.getUserRoles();
        authorities = roles.stream()
                .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getName().toUpperCase()))
                .toList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
