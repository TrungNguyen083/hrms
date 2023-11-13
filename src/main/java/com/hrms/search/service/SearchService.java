package com.hrms.search.service;

import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import com.hrms.search.document.EmployeeDocument;
import com.hrms.search.document.GlobalSearch;
import com.hrms.search.document.SkillDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class SearchService {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

//    public List<EmployeeDocument> searchEmployee(String searchText) {
//        SearchHits<EmployeeDocument> result = search(searchText, new String[]{"firstName", "lastName"},
//                                            "2", EmployeeDocument.class);
//        return result.getSearchHits().stream().map(item -> new EmployeeDocument(item.getContent().getId(),
//                item.getIndex(),
//                item.getContent().getFirstName(),
//                item.getContent().getLastName())).toList();
//    }
//
//    public List<SkillDocument> searchSkill(String searchText) {
//        SearchHits<SkillDocument> result = search(searchText, new String[]{"skillName", "skillSetId"},
//                                            "2", SkillDocument.class);
//        return result.getSearchHits().stream().map(item -> new SkillDocument(item.getContent().getId(),
//                item.getIndex(),
//                item.getContent().getSkillName(),
//                item.getContent().getSkillSetId())).toList();
//    }
//
//    private  <T> SearchHits<T> search(String searchText, String[] fields, String fuzzyString, Class<T> clazz) {
//        MultiMatchQuery multiMatchQuery = MultiMatchQuery.of(builder -> builder.query(searchText)
//                .fields(Arrays.asList(fields)).fuzziness(fuzzyString));
//
//        Query query = NativeQuery.builder().withQuery(
//                q -> q.multiMatch(multiMatchQuery)
//        ).build();
//
//        return elasticsearchOperations.search(query, clazz);
//    }

    public <T> List<T> search(String searchText, String[] fields, Class<T> clazz) {
        MultiMatchQuery multiMatchQuery = MultiMatchQuery.of(builder -> builder.query(searchText)
                .fields(Arrays.asList(fields)).fuzziness("2"));

        Query query = NativeQuery.builder().withQuery(
                q -> q.multiMatch(multiMatchQuery)
        ).build();

        SearchHits<T> result = elasticsearchOperations.search(query, clazz);

        return result.getSearchHits().stream()
                .map(SearchHit::getContent)
                .toList();

    }

    public GlobalSearch globalSearch(String searchText) {
        List<EmployeeDocument> employeeDocuments = search(searchText, new String[]{"firstName", "lastName"}, EmployeeDocument.class);
        List<SkillDocument> skillDocuments = search(searchText, new String[]{"skillName", "skillSetId"}, SkillDocument.class);
        return GlobalSearch.builder()
                .employeeDocuments(employeeDocuments)
                .skillDocuments(skillDocuments)
                .build();
    }
}
