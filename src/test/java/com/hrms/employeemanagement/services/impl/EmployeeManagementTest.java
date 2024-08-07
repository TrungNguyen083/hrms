//package com.hrms.employeemanagement.services.impl;
//
//import com.hrms.employeemanagement.dto.EmployeeDTO;
//import com.hrms.employeemanagement.models.Employee;
//import com.hrms.employeemanagement.repositories.EmployeeRepository;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.TimeZone;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Example;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//
//
//@ExtendWith(MockitoExtension.class)
//class   EmployeeManagementTest {
//
//    @Mock
//    private EmployeeRepository employeeRepository;
//
//    @InjectMocks
//    private EmployeeManagementServiceImpl employeeService;
//
//    @Test
//    void testGetNewEmployees() throws ParseException {
//        // Mocking data
//        Employee employee1 = new Employee();
//        employee1.setId(1);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//        employee1.setJoinedDate(dateFormat.parse("2021-03-01T17:00:00.000Z"));
//
//        Employee employee2 = new Employee();
//        employee2.setId(2);
//        employee2.setJoinedDate(dateFormat.parse("2023-03-01T17:00:00.000Z"));
//
//        List<Employee> employeeList = new ArrayList<>();
//        employeeList.add(employee1);
//        employeeList.add(employee2);
//
//        // Mocking the behavior of the repository
//        Page<Employee> employeePage = mock(Page.class);
//        when(employeePage.getContent()).thenReturn(employeeList);
//        when(employeeRepository.findAll(any(PageRequest.class))).thenReturn(employeePage);
//
//        // Calling the service method
//        List<EmployeeDTO> result = employeeService.getNewEmployees();
//
//        // Verifying that the repository method was called with the correct specification
//        verify(employeeRepository, times(1)).findAll(any(PageRequest.class));
//
//        Assertions.assertNotNull(result);
//    }
//}