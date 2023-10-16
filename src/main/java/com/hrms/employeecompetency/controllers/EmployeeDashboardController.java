package com.hrms.employeecompetency.controllers;

import com.hrms.employeecompetency.dto.EmployeeOverviewDto;
import com.hrms.employeecompetency.dto.EmployeeRating;
import com.hrms.employeecompetency.mapper.EmployeeMapperService;
import com.hrms.performancemanagement.model.EmployeePerformance;
import com.hrms.employeecompetency.services.CompetencyService;
import com.hrms.performancemanagement.service.PerformanceService;
import com.hrms.employeemanagement.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class EmployeeDashboardController {
    @Autowired
    EmployeeService employeeService;

    @Autowired
    PerformanceService performanceService;

    @Autowired
    CompetencyService competencyService;

    @Autowired
    EmployeeMapperService employeeMapperService;

    @QueryMapping
    public EmployeeOverviewDto employeeOverview(@Argument Integer id) {
        return employeeMapperService.employeeOverview(employeeService.findById(id));
    }

//    @QueryMapping
//    public CareerPathSummaryDto careerPathSummary(@Argument Integer id) {
//        return employeeMapperService.careerPathSummary(employeeService.findById(id));
//    }

    @QueryMapping
    public List<EmployeePerformance> topEmployeePerformance(@Argument Integer performanceCycleId) {
        return performanceService.findAllByPerformanceCycleId(performanceCycleId);
    }

    @QueryMapping
    public List<EmployeeRating> topEmployeeCompetencies(@Argument Integer competencyCycleId) {
        return competencyService.findAllByCompetencyCycleId(competencyCycleId);
    }



    public void getAtGlance(@Argument Integer employeeId) {

    }

    public void getOverallCompetencyScore(@Argument Integer employeeId) {

    }

    public void getTopSkills(@Argument Integer employeeId) {

    }

    public void getTopInterest(@Argument Integer employeeId) {

    }

    public void getImprovements(@Argument Integer employeeId) {

    }

    public void getPerformanceRatingScore(@Argument Integer employeeId) {

    }
}