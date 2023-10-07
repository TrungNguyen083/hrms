package com.hrms.employeemanagement.controllers.graphql;

import com.hrms.employeemanagement.models.Employee;
import com.hrms.employeemanagement.services.EmployeeService;
import com.hrms.employeemanagement.specifications.EmployeeSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GraphQLController {


    @Autowired
    private EmployeeService employeeService;

    @QueryMapping
    public EmployeeConnection findAllEmployees(@Argument int pageNo, @Argument int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        List<Employee> employees = employeeService.findAll(Specification.allOf(), pageable).getContent();
        long totalCount = employeeService.countEmployee();
        List<EmployeeEdge> employeeEdges = employees.stream()
                .map(employee -> new EmployeeEdge(employee, employee.getId()))
                .collect(Collectors.toList());
        long numberOfPages = (long) Math.ceil(((double) totalCount)/pageSize);
        Pagination pagination = new Pagination(pageNo, pageSize, totalCount, numberOfPages);
        return new EmployeeConnection(employeeEdges, pagination, totalCount);
    }

    @QueryMapping
    public long countEmployees() {
        return employeeService.countEmployee();
    }

    @QueryMapping
    public List<Employee> findEmployeeById(@Argument int id) {
        return employeeService.findAll(EmployeeSpecifications.hasId(id));
    }

    @QueryMapping
    public Iterable<Employee> findNewEmployeeOfMonth() {
        return employeeService.getNewEmployeeOfMonth();
    }

    @MutationMapping
    public Employee newEmployee(@Argument String firstName, @Argument String lastName,
                                @Argument String email, @Argument String gender, @Argument String dateOfBirth,
                                @Argument String phoneNumber, @Argument String address, @Argument String positionLevel) {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setGender(gender);
        employee.setDateOfBirth(dateOfBirth);
        employee.setPhoneNumber(phoneNumber);
        employee.setAddress(address);
        employeeService.saveEmployee(employee);
        return employee;
    }

    @MutationMapping
    public boolean deleteEmployee(@Argument int id) {
        employeeService.deleteEmployeeById(id);
        return true;
    }

    @MutationMapping
    public Employee updateEmployee(@Argument int id, @Argument String firstName, @Argument String lastName,
                                   @Argument String email, @Argument String gender, @Argument String dateOfBirth,
                                   @Argument String phoneNumber, @Argument String address, @Argument String positionLevel) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setGender(gender);
        employee.setDateOfBirth(dateOfBirth);
        employee.setPhoneNumber(phoneNumber);
        employee.setAddress(address);
        employeeService.saveEmployee(employee);
        return employee;
    }
}