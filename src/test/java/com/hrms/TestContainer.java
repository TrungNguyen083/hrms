package com.hrms;

import com.hrms.global.mapper.HrmsMapper;
import com.hrms.usermanagement.dto.SignupDto;
import com.hrms.usermanagement.repository.UserRepository;
import com.hrms.usermanagement.repository.UserRoleRepository;
import com.hrms.usermanagement.service.UserService;
import com.hrms.usermanagement.specification.UserSpecification;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestContainer {
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql"))
            .withExposedPorts(3306)
            .withDatabaseName("hrms")
            .withUsername("root")
            .withPassword("root");

    @Container
    static GenericContainer<?> elastic = new GenericContainer<>(DockerImageName.parse("elasticsearch:8.10.4"))
            .withExposedPorts(9201)
            .withEnv("discovery.type", "single-node");

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserSpecification userSpecification;
    @Autowired
    private HrmsMapper modelMapper;
    @Autowired
    private EntityManager em;

    private UserService userService;

    @BeforeEach
    void setUp() {
        this.userService = new UserService(userRepository, userRoleRepository, passwordEncoder, userSpecification, modelMapper, em);
    }

    @Test
    public void test() {
        assert mysql.isRunning() == true;
        assert userService != null;
    }

    @Test
    public void givenSignupDto_whenCreateUser_shouldReturnUser() throws Exception {
        String username = "test1";
        String password = "test1";
        SignupDto signupDto = new SignupDto(username, password);

        var u = userService.createUser(signupDto);
        Assertions.assertEquals(u.getUsername(), username);
    }

    @Test
    public void givenUserId_whenFindById_shouldException() throws Exception {
        Assertions.assertThrows(Exception.class, () -> {
            userService.getUser(999);
        });
    }
}
