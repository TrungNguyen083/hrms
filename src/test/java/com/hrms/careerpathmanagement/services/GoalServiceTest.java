package com.hrms.careerpathmanagement.services;

import com.hrms.careerpathmanagement.models.Goal;
import com.hrms.careerpathmanagement.repositories.GoalRepository;
import com.hrms.employeemanagement.models.Employee;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
@Transactional
class GoalServiceTest {

    @Autowired
    GoalService goalService;

    @MockBean
    GoalRepository goalRepository;

    @BeforeEach
    void setUp() {
        Employee e1 = new Employee();
        e1.setId(1);
        e1.setFirstName("John");
        e1.setLastName("Doe");

        Employee e2 = new Employee();
        e2.setId(2);
        e2.setFirstName("Jane");
        e2.setLastName("Davis");

        Goal g1 = new Goal();
        g1.setId(1);
        g1.setEmployee(e1);
        g1.setStatus("COMPLETED");

        Goal g2 = new Goal();
        g2.setId(2);
        g2.setEmployee(e2);
        g2.setStatus("NOTSTART");

        List<Goal> goals = new ArrayList<>();
        goals.add(g1);
        goals.add(g2);

    }

    @Test
    void getAllGoals() {
        assert goalService.getGoals(9, 1, 5).data().size() == 2;
        assert goalService.getGoals(1, 1, 5).data().size() == 1;
    }

    @Test
    public void getCountChart() {
        assert goalService.getGoalsStatusStatistic(1, 8).getDatasets().size() > 0;
    }

    @Test
    void getGoalsStatusStatistic() {

    }

    @Test
    void getGoalsByEmployee() {
    }
}