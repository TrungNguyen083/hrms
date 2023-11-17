package com.hrms.search.service;

import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import com.hrms.search.configuration.SearchConfig;
import com.hrms.search.document.*;
import com.hrms.search.dto.ResultItemDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
public class SearchService {

    @Autowired
    ElasticsearchOperations elasticsearchOperations;

    @Autowired
    SearchConfig searchConfig;

    public <T> SearchHits<T> getSearchResult(Class<T> clazz, String searchText, String... fields) {
        MultiMatchQuery multiMatchQuery = MultiMatchQuery.of(builder -> builder.query(searchText)
                .fields(Arrays.asList(fields)).fuzziness("AUTO"));

        Query query = NativeQuery.builder().withQuery(
                q -> q.multiMatch(multiMatchQuery)
        ).build();

        return elasticsearchOperations.search(query, clazz);
    }

    public List<ResultItemDTO> searchEmployee(String searchText) {
        return getSearchResult(EmployeeDocument.class, searchText, SearchConfig.Employee.FIELDS.toArray(String[]::new))
                .stream().map(searchHit -> {
                    var item = searchHit.getContent();
                    return new ResultItemDTO(item.getId(), item.getFullName(), null,
                            SearchConfig.Employee.INDEX, searchHit.getScore());
                }).toList();
    }

    public List<ResultItemDTO> searchSkillSet(String searchText) {
        return getSearchResult(SkillSetDocument.class, searchText, SearchConfig.SkillSet.FIELDS.toArray(String[]::new))
                .stream().map(searchHit -> {
                    var item = searchHit.getContent();
                    return new ResultItemDTO(item.getId(), item.getSkillSetName(), null,
                            SearchConfig.SkillSet.INDEX, searchHit.getScore());
                }).toList();
    }

    public List<ResultItemDTO> searchCompetency(String searchText) {
        return getSearchResult(CompetencyDocument.class, searchText, SearchConfig.Competency.FIELDS.toArray(String[]::new))
                .stream().map(searchHit -> {
                    var item = searchHit.getContent();
                    return new ResultItemDTO(item.getId(), item.getCompetencyName(), item.getDescription(),
                            SearchConfig.Competency.INDEX, searchHit.getScore());
                }).toList();
    }

    public List<ResultItemDTO> searchPosition(String searchText) {
        return getSearchResult(
                PositionDocument.class,
                searchText,
                SearchConfig.Position.FIELDS.toArray(String[]::new))
                .stream().map(searchHit -> {
                    var item = searchHit.getContent();
                    return new ResultItemDTO(item.getId(), item.getPositionName(), null,
                            SearchConfig.Position.INDEX, searchHit.getScore());
                }).toList();
    }

    public List<ResultItemDTO> searchCompetencyCycle(String searchText) {
        return getSearchResult(
                CompetencyCycleDocument.class,
                searchText,
                SearchConfig.CompetencyCycle.FIELDS.toArray(String[]::new))
                .stream().map(searchHit -> {
                    var item = searchHit.getContent();
                    return new ResultItemDTO(item.getId(), item.getCompetencyCycleName(), item.getDescription(),
                            SearchConfig.CompetencyCycle.INDEX, searchHit.getScore());
                }).toList();
    }

    public List<ResultItemDTO> searchPerformanceCycle(String searchText) {
        return getSearchResult(
                PerformanceCycleDocument.class,
                searchText,
                SearchConfig.PerformanceCycle.FIELDS.toArray(String[]::new))
                .stream().map(searchHit -> {
                    var item = searchHit.getContent();
                    return new ResultItemDTO(item.getId(), item.getPerformanceCycleName(), item.getDescription(),
                            SearchConfig.PerformanceCycle.INDEX, searchHit.getScore());
                }).toList();
    }

    public List<ResultItemDTO> searchGlobal(String searchText) {
        List<ResultItemDTO> employees = searchEmployee(searchText);
        List<ResultItemDTO> skillSets = searchSkillSet(searchText);
        List<ResultItemDTO> competencies = searchCompetency(searchText);
        List<ResultItemDTO> positions = searchPosition(searchText);
        List<ResultItemDTO> competencyCycles = searchCompetencyCycle(searchText);
        List<ResultItemDTO> performanceCycles = searchPerformanceCycle(searchText);
        return Stream.of(employees, skillSets, competencies, positions, competencyCycles, performanceCycles)
                .flatMap(List::stream).toList()
                .stream().sorted((o1, o2) -> o2.getRankingScore().compareTo(o1.getRankingScore())).toList();
    }

    public List<Object> getIndicesConfig() throws FileNotFoundException, ParseException {
        FileReader reader = new FileReader("src/main/java/com/hrms/search/configuration/tagsinfo.json");
        JSONParser parser = new JSONParser(reader);
        return parser.parseArray();
    }
}