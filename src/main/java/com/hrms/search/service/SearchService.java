package com.hrms.search.service;

import com.hrms.search.document.Employee;
import com.hrms.search.repository.EmpRepository;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SearchService {

    @Autowired
    private EmpRepository employeeRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;


    public void createEmployeeIndexBulk(final List<Employee> employees) {
        employeeRepository.saveAll(employees);
    }

    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public List<Employee> searchEmployee(String searchText) {
        SearchHits<Employee> searchHits = elasticsearchOperations.search(
                new NativeSearchQueryBuilder()
                        .withQuery(
                                QueryBuilders.boolQuery()
                                        .should(QueryBuilders.matchQuery("firstName", searchText).fuzziness(Fuzziness.AUTO))
                                        .should(QueryBuilders.matchQuery("lastName", searchText).fuzziness(Fuzziness.AUTO))
                        )
                        .build(),
                Employee.class
        );

        return searchHits.stream().map(SearchHit::getContent).toList();
    }
}
