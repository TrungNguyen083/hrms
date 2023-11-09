package com.hrms.search.service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import com.hrms.search.document.Employee;
import com.hrms.search.repository.EmpRepository;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    @Autowired
    private EmpRepository employeeRepository;


    public void createEmployeeIndexBulk(final List<Employee> employees) {
        employeeRepository.saveAll(employees);
    }

    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public List<Employee> searchEmployee(String searchText) {
        return null;
    }
}
