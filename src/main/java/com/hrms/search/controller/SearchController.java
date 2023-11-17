package com.hrms.search.controller;

import com.hrms.search.dto.ResultItemDTO;
import com.hrms.search.service.SearchService;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {


    @Autowired
    SearchService searchService;

    @GetMapping("/employee")
    public List<ResultItemDTO> searchEmployee(@RequestParam String searchText) {
        return searchService.searchEmployee(searchText);
    }

    @GetMapping("/skill-set")
    public List<ResultItemDTO> searchSkillSet(@RequestParam String searchText) {
        return searchService.searchSkillSet(searchText);
    }

    @GetMapping("/competency")
    public List<ResultItemDTO> searchCompetency(@RequestParam String searchText) {
        return searchService.searchCompetency(searchText);
    }

    @GetMapping("/position")
    public List<ResultItemDTO> searchPosition(@RequestParam String searchText) {
        return searchService.searchPosition(searchText);
    }

    @GetMapping("/competency-cycle")
    public List<ResultItemDTO> searchCompetencyCycle(@RequestParam String searchText) {
        return searchService.searchCompetencyCycle(searchText);
    }

    @GetMapping("/performance-cycle")
    public List<ResultItemDTO> searchPerformanceCycle(@RequestParam String searchText) {
        return searchService.searchPerformanceCycle(searchText);
    }

    @GetMapping("/global")
    public List<ResultItemDTO> searchGlobal(@RequestParam String searchText) {
        return searchService.searchGlobal(searchText);
    }

    @GetMapping("/categories")
    public List<Object> getCategories() throws FileNotFoundException, ParseException {
        return searchService.getIndicesConfig();
    }
}
