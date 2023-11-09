package com.hrms.search.controller;

import com.hrms.search.document.Employee;
import com.hrms.search.repository.EmpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    EmpRepository employeeRepository;

    @PostMapping("/employee/add")
    public void addEmployee(@RequestBody Employee employee) {
        employeeRepository.save(employee);
    }

    @GetMapping("/employee/{searchText}")
    public List<Employee> search(@PathVariable String searchText) {
        var result = employeeRepository.findByFirstName(searchText);
        result.addAll(employeeRepository.findByLastName(searchText));
        return result;
    }
}
