package com.hrms.usermanagement.controller;

import com.hrms.usermanagement.dto.Token;
import com.hrms.usermanagement.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class LoginController {
    private final AuthenticationService authenticationService;

    @Autowired
    public LoginController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @QueryMapping
    public Token login(@Argument String username, @Argument String password)
            throws Exception {
        var token = authenticationService.login(username, password);
        var user = authenticationService.getUser(username);
        return new Token(user, token);
    }

}
