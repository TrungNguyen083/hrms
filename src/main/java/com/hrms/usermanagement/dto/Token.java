package com.hrms.usermanagement.dto;

import com.hrms.usermanagement.model.User;

public record Token(User owner, String token) {
}
