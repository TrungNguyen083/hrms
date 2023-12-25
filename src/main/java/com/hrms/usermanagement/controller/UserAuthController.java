package com.hrms.usermanagement.controller;

import com.hrms.usermanagement.security.HRMSUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/auth")
public class UserAuthController {
    private HRMSUserDetailsService userDetailsService;

    @Autowired
    public UserAuthController(HRMSUserDetailsService userService) {
        this.userDetailsService = userService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        var user = userDetailsService.loadUserByUsername(username);
        return ResponseEntity.ok(user);
    }
}
