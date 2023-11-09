package com.hrms.search.repository;

import com.hrms.search.document.Employee;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpRepository extends ElasticsearchRepository<Employee, String> {

    List<Employee> findByFirstName(String firstName);
    List<Employee> findByLastName(String lastName);

}
