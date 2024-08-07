//package com.hrms.employeemanagement.services.impl;
//
//import com.hrms.careerpathmanagement.dto.PercentageChangeDTO;
//import com.hrms.employeemanagement.dto.EmployeeInputDTO;
//import com.hrms.employeemanagement.dto.pagination.EmployeePagingDTO;
//import com.hrms.employeemanagement.models.Employee;
//import com.hrms.employeemanagement.services.EmployeeManagementService;
//import com.hrms.global.dto.BarChartDTO;
//import com.hrms.global.mapper.HrmsMapper;
//import com.hrms.global.paging.PagingInfo;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//
//import java.util.List;
//
//
//@SpringJUnitConfig
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class EmployeeManagementIntegrationTest {
//
//    @Autowired
//    private EmployeeManagementService employeeService;
//
//    @Test
//    void testGetAllEmployees() {
//        List<Employee> result = employeeService.getAllEmployees();
//        Assertions.assertEquals(13, result.size());
//    }
//
//    @Test
//    void testGetAllEmployeesHaveDepartment() {
//        List<Employee> result = employeeService.getAllEmployeesHaveDepartment();
//        Assertions.assertEquals(8, result.size());
//    }
//
//    @Test
//    void testGetEmployeesInDepartment() {
//        List<Employee> result = employeeService.getEmployeesInDepartment(1);
//        Assertions.assertEquals(3, result.size());
//    }
//
//    @Test
//    void testFindEmployee() {
//        Employee result = employeeService.findEmployee(1);
//        Assertions.assertEquals("Ly Luu", result.getFullName());
//    }
//
//    @Test
//    void testFilterEmployees() {
//        EmployeePagingDTO result = employeeService.filterEmployees(List.of(1), null, null, "Messi", 1,10);
//        Assertions.assertEquals(1, result.data().size());
//    }
//
//    @Test
//    void testGetHeadcountsStatistic() {
//        PercentageChangeDTO result = employeeService.getHeadcountsStatistic();
//        Assertions.assertEquals(true, result.getIsIncreased());
//        Assertions.assertTrue(result.getDiffPercent() > 0);
//        Assertions.assertEquals(13, result.getData());
//    }
//
//    @Test
//    void testGetHeadcountChartData() {
//        BarChartDTO result = employeeService.getHeadcountChartData();
//        Assertions.assertEquals(3, result.getItems().size());
//    }
//
//    @Test
//    void testCreateEmployee() {
//        EmployeeInputDTO employee = new EmployeeInputDTO();
//        employee.setFirstName("Test First Name");
//        employee.setLastName("Test Last Name");
//        Employee result = employeeService.createEmployee(employee);
//        Assertions.assertNotNull(result);
//        Assertions.assertEquals("Test First Name", result.getFirstName());
//        Assertions.assertEquals("Test Last Name", result.getLastName());
//    }
//
//    @Test
//    void testGetProfilePicture() {
//        String result = employeeService.getProfilePicture(4);
//        Assertions.assertNotNull(result);
//    }
//}
