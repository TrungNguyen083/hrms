package com.hrms.usermanagement.controller;

import com.hrms.usermanagement.dto.AuthRequest;
import com.hrms.usermanagement.dto.SignupDto;
import com.hrms.usermanagement.dto.Token;
import com.hrms.usermanagement.dto.UserDtoPagination;
import com.hrms.usermanagement.service.JwtService;
import com.hrms.usermanagement.service.UserInfoService;
import com.hrms.usermanagement.service.UserService;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserInfoService service;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Autowired
    public AuthController(UserInfoService service, JwtService jwtService,
                          AuthenticationManager authenticationManager, UserService userService) {
        this.service = service;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/createUser")
    public Boolean createUser(@RequestBody SignupDto signupDto) throws Exception {
        return service.createUser(signupDto);
    }

    @PostMapping("/generateToken")
    public Token login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return new Token(null, jwtService.generateToken(authRequest.getUsername()));
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
}
