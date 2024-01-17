package com.hrms.usermanagement.dto;


public record Token(UserDto owner, String token) {
}
