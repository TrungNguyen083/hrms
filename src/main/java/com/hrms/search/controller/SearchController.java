package com.hrms.search.controller;

import com.hrms.search.document.EmployeeDocument;
import com.hrms.search.document.GlobalSearch;
import com.hrms.search.document.SkillDocument;
import com.hrms.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private SearchService searchService;

//    @GetMapping("/employee/{searchText}")
//    public List<EmployeeDocument> searchEmployee(@PathVariable String searchText) {
//        return searchService.searchEmployee(searchText);
//    }
//
//    @GetMapping("/skill/{searchText}")
//    public List<SkillDocument> searchSkill(@PathVariable String searchText) {
//        return searchService.searchSkill(searchText);
//    }

    @GetMapping("/global/{searchText}")
    public GlobalSearch globalSearch(@PathVariable String searchText) {
        return searchService.globalSearch(searchText);
    }
}
