package com.hrms.usermanagement.model;

import com.hrms.employeemanagement.models.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.Collection;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class  User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private Boolean isEnabled;

    @Column(name = "created_at")
    private Date createdAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<UserRole> userRoles;

    public User(User user) {
        this.userId = user.userId;
        this.username = user.username;
        this.password = user.password;
        this.isEnabled = user.isEnabled;
        this.createdAt = user.createdAt;
        this.userRoles = user.userRoles;
    }
}
