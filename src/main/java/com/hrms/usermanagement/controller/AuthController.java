package com.hrms.usermanagement.controller;

import com.hrms.usermanagement.dto.AuthRequest;
import com.hrms.usermanagement.service.JwtService;
import com.hrms.usermanagement.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class AuthController {

    private final UserInfoService service;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserInfoService service, JwtService jwtService,
                          AuthenticationManager authenticationManager) {
        this.service = service;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @MutationMapping(name = "createUser")
    public Boolean createUser(@Argument AuthRequest authRequest) throws Exception {
        return service.createUser(authRequest);
    }

    @MutationMapping(name = "generateToken")
    public String login(@Argument AuthRequest authRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            List<String> authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
            return jwtService.generateToken(authRequest.getUsername(), authorities);
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }
}
