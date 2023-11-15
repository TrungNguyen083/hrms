package com.hrms.search.repository;

import com.hrms.search.document.SkillSetDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SkillSetSearchRepository extends ElasticsearchRepository<SkillSetDocument, String> {
}
