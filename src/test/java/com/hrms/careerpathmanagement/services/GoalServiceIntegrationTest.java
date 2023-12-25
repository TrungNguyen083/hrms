package com.hrms.careerpathmanagement.services;

import com.hrms.careerpathmanagement.models.CompetencyCycle;
import com.hrms.careerpathmanagement.models.Goal;
import com.hrms.careerpathmanagement.repositories.CompetencyCycleRepository;
import com.hrms.careerpathmanagement.repositories.GoalRepository;
import com.hrms.careerpathmanagement.services.GoalService;
import com.hrms.careerpathmanagement.specification.CompetencySpecification;
import com.hrms.employeemanagement.models.Department;
import com.hrms.employeemanagement.models.Employee;
import com.hrms.employeemanagement.repositories.DepartmentRepository;
import com.hrms.employeemanagement.repositories.EmployeeDamInfoRepository;
import com.hrms.employeemanagement.repositories.EmployeeRepository;
import com.hrms.employeemanagement.specification.EmployeeSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Date;

@SpringBootTest
@Testcontainers
public class GoalServiceIntegrationTest {
    @Autowired
    private GoalRepository goalRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeDamInfoRepository employeeDamInfoRepository;
    @Autowired
    private EmployeeSpecification employeeSpecification;
    @Autowired
    private CompetencySpecification competencySpecification;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private CompetencyCycleRepository competencyCycleRepository;
    private GoalService goalService;

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:latest")
            .withExposedPorts(3306)
            .withDatabaseName("hrms")
            .withUsername("root")
            .withPassword("root");

    @BeforeEach
    void init() {
        goalService = new GoalService(goalRepository,
                employeeDamInfoRepository,
                employeeRepository,
                employeeSpecification,
                competencySpecification);

        Department department = Department.builder()
                .id(1)
                .departmentName("Backend")
                .build();

        departmentRepository.save(department);

        Employee e1 = Employee.builder()
                .id(1)
                .firstName("Ly")
                .lastName("Nguyen")
                .department(department)
                .insertionTime(new Date())
                .modificationTime(new Date())
                .build();

        employeeRepository.save(e1);

        CompetencyCycle cycle = CompetencyCycle.builder()
                .id(1)
                .competencyCycleName("Cycle 1")
                .description("Description 1")
                .insertionTime(new Date())
                .modificationTime(new Date())
                .build();

        competencyCycleRepository.save(cycle);

        Goal goal = Goal.builder()
                .id(1)
                .employee(e1)
                .title("Goal 1")
                .description("Description 1")
                .status("NOTSTART")
                .competencyCycle(cycle)
                .build();

        goalRepository.save(goal);
    }

    @Test
    public void givenDepartmentIdAndCycleId_whenGetGoals_thenReturnGoalsPage() {
        var goals = goalService.getEmployeesGoals(1,1, 0, 1);
        assert goals.data().size() == 1;
    }

    @Test
    public void givenDepartmentIdAndCycleId_whenCountGoalsCompleted_thenReturnCountAndPercent() {
        var countAndPercent = goalService.countGoalsCompleted(1, 1);
        assert countAndPercent.percentage() == 0;
    }

    @Test
    public void givenEmpId_whenGetGoals_thenReturnGoalsPage() {
        Goal goal = Goal.builder()
                .id(2)
                .employee(employeeRepository.findById(1).get())
                .title("Goal 2")
                .description("Description 2")
                .status("NOTSTART")
                .competencyCycle(competencyCycleRepository.findById(1).get())
                .build();
        goalRepository.save(goal);

        var goals = goalService.getGoals(1, 1, 10);

        assert goals != null;
        assert goals.data().size() == 2;
    }

    @Test
    public void givenDepIdAndCycleId_whenGetStatistic_thenReturnChartDto() {
        var chartDto = goalService.getGoalsStatusStatistic(1, 1);
        assert chartDto != null;
        assert chartDto.getLabels().size() == 1;
    }
}
