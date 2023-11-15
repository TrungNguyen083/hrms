package com.hrms.search.repository;

import com.hrms.search.document.EmployeeDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeSearchRepository extends ElasticsearchRepository<EmployeeDocument, String> {
}
