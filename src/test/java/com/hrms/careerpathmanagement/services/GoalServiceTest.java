//package com.hrms.careerpathmanagement.services;
//
//import com.hrms.careerpathmanagement.models.Goal;
//import com.hrms.careerpathmanagement.projection.GoalProjection;
//import com.hrms.careerpathmanagement.repositories.GoalRepository;
//import com.hrms.careerpathmanagement.specification.CompetencySpecification;
//import com.hrms.global.models.Department;
//import com.hrms.employeemanagement.models.Employee;
//import com.hrms.employeemanagement.repositories.EmployeeDamInfoRepository;
//import com.hrms.employeemanagement.repositories.EmployeeRepository;
//import com.hrms.employeemanagement.specification.EmployeeSpecification;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.*;
//import org.springframework.data.jpa.domain.Specification;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.*;
//import static org.mockito.Mockito.times;
//
//@ExtendWith(MockitoExtension.class)
//class GoalServiceTest {
//
//    private EmployeeRepository employeeRepository;
//    private GoalRepository goalRepository;
//    private EmployeeDamInfoRepository employeeDamInfoRepository;
//    private EmployeeSpecification employeeSpecification;
//    private CompetencySpecification competencySpecification;
//
//    private GoalService goalService;
//    static Employee employee = new Employee();
//    @BeforeEach
//    void setUp() {
//        Employee e1 = new Employee();
//        e1.setId(1);
//        e1.setFirstName("John");
//        e1.setLastName("Doe");
//
//        Employee e2 = new Employee();
//        e2.setId(2);
//        e2.setFirstName("Jane");
//        e2.setLastName("Davis");
//
//        Goal g1 = new Goal();
//        g1.setId(1);
//        g1.setEmployee(e1);
//        g1.setStatus("COMPLETED");
//
//        Goal g2 = new Goal();
//        g2.setId(2);
//        g2.setEmployee(e2);
//        g2.setStatus("NOTSTART");
//
//        List<Goal> goals = new ArrayList<>();
//        goals.add(g1);
//        goals.add(g2);
//
//        employeeRepository = mock(EmployeeRepository.class);
//        goalRepository = mock(GoalRepository.class);
//        employeeDamInfoRepository = mock(EmployeeDamInfoRepository.class);
//        employeeSpecification = mock(EmployeeSpecification.class);
//        competencySpecification = mock(CompetencySpecification.class);
//        goalService = new GoalService(goalRepository, employeeDamInfoRepository, employeeRepository, employeeSpecification, competencySpecification);
//
//        Department department = new Department();
//        department.setId(1);
//
//        employee.setId(1);
//        employee.setFirstName("Ly");
//        employee.setLastName("Luu");
//        employee.setDepartment(department);
//
//        List<GoalProjection> goalProjections = Collections.singletonList(mock(GoalProjection.class));
//
//        when(goalRepository.findAllByEmployeeId(1, any(Pageable.class))).thenReturn(new PageImpl<>(goalProjections));
//    }
//
//    @Test
//    void getAllGoals() {
//        when(goalRepository.findAllByEmployeeId(1, any(Pageable.class)))
//                .thenReturn(new PageImpl<>(List.of()));
//        assert goalService.getGoals(9, 1, 5).data().size() == 2;
//        assert goalService.getGoals(1, 1, 5).data().size() == 1;
//    }
//
//    @Test
//    public void getCountChart() {
//        assert goalService.getGoalsStatusStatistic(1, 8).getDatasets().size() > 0;
//    }
//
//    @Test
//    void getService_Existing() {
//        assertNotNull(goalService);
//    }
//
//    @Test
//    void getGoalsByEmployee() {
//        GoalProjection goalProjection = mock(GoalProjection.class);
//        List<GoalProjection> mockGoals = List.of(goalProjection);
//
//        int pageNo = 0;
//        int pageSize = 10;
//        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("updatedAt").descending());
//
//        when(goalRepository.findAllByEmployeeId(1, pageable)).thenReturn(new PageImpl<>(mockGoals, pageable, mockGoals.size()));
//
//        var result = goalService.getGoals(1, pageNo + 1, pageSize);
//
//        verify(goalRepository, times(1)).findAllByEmployeeId(1, pageable);
//        assertEquals(1, result.data().size());
//    }
//
//    @Test
//    void getGoalStatusStatistic_shouldReturnNotNull() {
//        var result = goalService.getGoalsStatusStatistic(1, 1);
//        assertNotNull(result);
//    }
//
//    @Test
//    void countGoalCompeleted_shouldReturnNotNullChart() {
//        when(employeeSpecification.hasDepartmentId(anyInt())).thenReturn((root, query, cb) -> cb.equal(root.get("department").get("id"), 1));
//        when(competencySpecification.hasCycleId(anyInt())).thenReturn((root, query, cb) -> cb.equal(root.get("cycle").get("id"), 1));
//        when(goalRepository.count(any(Specification.class))).thenReturn(1L);
//
//        var result = goalService.countGoalsCompleted(1, 1);
//
//        assertNotNull(result);
//    }
//
//    @Test
//    void getGoalsByDepartment() {
//        GoalProjection goalProjection = mock(GoalProjection.class);
//        List<GoalProjection> mockGoals = List.of(goalProjection);
//
//        int pageNo = 0;
//        int pageSize = 10;
//        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("updatedAt").descending());
//
//        var employeeIds = List.of(1);
//        var emps = List.of(employee);
//
//        //when(employeeRepository.findAllByIdIn(employeeIds, NameOnly.class)).then
//        when(goalRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(new PageImpl<>(mockGoals, pageable, mockGoals.size()));
//    }
//}