package com.hrms.search.repository;

import com.hrms.search.document.CompetencyDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetencySearchRepository extends ElasticsearchRepository<CompetencyDocument, String> {
}
