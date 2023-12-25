package com.hrms.careerpathmanagement;

import com.hrms.global.mapper.HrmsMapper;
import com.hrms.usermanagement.dto.SignupDto;
import com.hrms.usermanagement.repository.UserRepository;
import com.hrms.usermanagement.repository.UserRoleRepository;
import com.hrms.usermanagement.service.UserService;
import com.hrms.usermanagement.specification.UserSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceIntegrationTest {
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql:latest"))
            .withExposedPorts(3306)
            .withDatabaseName("hrms")
            .withUsername("root")
            .withPassword("root");

    @Container
    static ElasticsearchContainer elastic = new ElasticsearchContainer(DockerImageName.parse("elasticsearch:8.7.1"))
            .withExposedPorts(9201);

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
        elastic.start();
        mysql.start();
        this.userService = new UserService(userRepository, userRoleRepository, passwordEncoder, userSpecification, modelMapper, em);
    }

    @Test
    void contextLoads() {
        assert mysql.isRunning() == true;
        assert elastic.isRunning() == true;
        assert userService != null;
    }
    @Test
    void createUser_shouldReturnUser() throws Exception {
        String username = "test";
        String password = "test";

        SignupDto signupDto = new SignupDto(username, password);
        var user = userService.createUser(signupDto);

        assertNotNull(user);
    }

    @Test
    void getUser_shouldReturnUser() throws Exception {
        Integer userId = 1;

        var user = userService.getUser(userId);

        assertNotNull(user);
    }

    @Test
    void searchUser_shouldReturnNotNull() {
        String searchText = "test";
        var result = userService.searchUsers(searchText, null, null, PageRequest.of(0, 10));

        assertNotNull(result);
    }

    @Test
    @Transactional
    void updateUserRole_shouldReturnManagerRole() throws Exception {
        Integer userId = 1;
        Integer roleId = 2;

        var isUpdated = userService.updateUsers(List.of(userId), Boolean.TRUE, List.of(roleId));

        assertEquals(isUpdated, true);
    }
}
