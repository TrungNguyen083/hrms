package com.hrms.careerpathmanagement.repositories;

import com.hrms.careerpathmanagement.models.CompetencyCycle;
import com.hrms.careerpathmanagement.models.Goal;
import com.hrms.employeemanagement.models.Employee;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class GoalRepositoryTest {
    @Autowired
    private GoalRepository goalRepository;

    @Test
    void findAllByEmployeeId() {
        Goal g1 = new Goal();
        g1.setId(1);
        g1.setEmployee(new Employee());
        g1.setStatus("COMPLETED");
        g1.setCompetencyCycle(new CompetencyCycle());

        goalRepository.save(g1);

        assertEquals(1, goalRepository.findAllByEmployeeId(1, PageRequest.of(0, 10)).getTotalElements());
    }

    @Test
    void countByDepartmentCycleStatus() {
    }

    @Test
    void countByEmployeeDepartmentIdAndCompetencyCycleIdAndStatusIs() {
    }

    @Test
    void findAllByDepartmentAndCycle() {
    }

    @Test
    void findAllByEmployeeDepartmentIdAndCompetencyCycleId() {
    }

    @Test
    void findAllByEmployeeDepartmentIdAndCompetencyCycleIdOrderByUpdatedAt() {
    }
}