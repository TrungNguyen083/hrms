//package com.hrms.careerpathmanagement.repositories;
//
//import com.hrms.careerpathmanagement.dto.pagination.EmployeeGoalPagination;
//import com.hrms.careerpathmanagement.dto.pagination.GoalPagination;
//import com.hrms.global.models.CompetencyCycle;
//import com.hrms.careerpathmanagement.models.Goal;
//import com.hrms.careerpathmanagement.projection.GoalProjection;
//import com.hrms.careerpathmanagement.services.GoalService;
//import com.hrms.careerpathmanagement.specification.CompetencySpecification;
//import com.hrms.employeemanagement.models.Employee;
//import com.hrms.employeemanagement.repositories.EmployeeDamInfoRepository;
//import com.hrms.employeemanagement.repositories.EmployeeRepository;
//import com.hrms.employeemanagement.specification.EmployeeSpecification;
//import lombok.Data;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.data.domain.*;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class GoalRepositoryTest {
//    private GoalRepository goalRepository;
//    private EmployeeRepository employeeRepository;
//    private EmployeeDamInfoRepository employeeDamInfoRepository;
//    private EmployeeSpecification employeeSpecification;
//    private CompetencySpecification competencySpecification;
//    private GoalService goalService;
//
//    private final Employee employee = new Employee();
//
//    @BeforeEach
//    void init() {
//        goalRepository = mock(GoalRepository.class);
//        employeeRepository = mock(EmployeeRepository.class);
//        employeeDamInfoRepository = mock(EmployeeDamInfoRepository.class);
//        employeeSpecification = mock(EmployeeSpecification.class);
//        competencySpecification = mock(CompetencySpecification.class);
//        goalService = new GoalService(goalRepository, employeeDamInfoRepository, employeeRepository, employeeSpecification, competencySpecification);
//
//        employee.setId(1);
//        employee.setFirstName("Ly");
//    }
//
//
//
//    @Test
//    void countByDepartmentCycleStatus() {
//        when(goalRepository.countByDepartmentCycleStatus(1, 1, "COMPLETED")).thenReturn(1L);
//        assertEquals(1, goalRepository.countByDepartmentCycleStatus(1, 1, "COMPLETED"));
//    }
//
//    @Test
//    void countByEmployeeDepartmentIdAndCompetencyCycleIdAndStatusIs() {
//    }
//
//    @Test
//    void findAllByDepartmentAndCycle() {
//    }
//
//    @Test
//    void findAllByEmployeeDepartmentIdAndCompetencyCycleId() {
//    }
//
//    @Test
//    void findAllByEmployeeDepartmentIdAndCompetencyCycleIdOrderByUpdatedAt() {
//    }
//}