package com.hrms.performancemanagement.controllers;

import com.hrms.careerpathmanagement.dto.EmployeePotentialPerformanceDTO;
import com.hrms.employeemanagement.dto.EmployeeRatingPagination;
import com.hrms.performancemanagement.dto.PerformanceByJobLevalChartDTO;
import com.hrms.performancemanagement.model.PerformanceEvaluation;
import com.hrms.performancemanagement.services.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@CrossOrigin(origins = "*")
public class PerformanceController {
    @Autowired
    PerformanceService performanceService;
    //TODO: get all performance cycles -> Do not caching
//    @QueryMapping(name = "employeePerformanceRatingScore")
//    //
//    public EmployeePotentialPerformanceDTO getEmployeePerformanceRatingScore(@Argument(name = "employeeId") Integer empId,
//                                                                             @Argument int pageNo,
//                                                                             @Argument int pageSize)
//    {
//        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
//        Page<PerformanceEvaluation> empPerformances = performanceService.getPerformanceEvaluations(empId, pageable);
//        List<LabelScoreDTO> data = empPerformances.stream()
//                .map(item ->
//                        new LabelScoreDTO(item.getPerformanceCycle().getPerformanceCycleName(), item.getFinalAssessment()))
//                .toList();
//        Pagination pagination = setupPaging(empPerformances.getTotalElements(), pageNo, pageSize);
//        return new (data, pagination);
//    }


    @QueryMapping(name = "performanceByJobLevel")
    public PerformanceByJobLevalChartDTO getPerformanceByJobLevel(@Argument Integer positionId,
                                                                  @Argument Integer cycleId)
    {
        return performanceService.getPerformanceByJobLevel(positionId, cycleId);
    }

    @QueryMapping(name = "employeesPotentialPerformance")
    public List<EmployeePotentialPerformanceDTO> getPotentialAndPerformance(@Argument Integer departmentId,
                                                                            @Argument Integer cycleId)
    {
        return performanceService.getPotentialAndPerformance(departmentId, cycleId);
    }


    /**
     * HR Dashboard - Get top performers
     * @param cycleId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @QueryMapping(name = "topPerformers")
    public EmployeeRatingPagination getPerformanceRating(@Argument Integer cycleId,
                                                         @Argument Integer pageNo,
                                                         @Argument Integer pageSize)
    {
        Sort sort = Sort.by(Sort.Direction.DESC, "finalAssessment");
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return performanceService.getPerformanceRating(cycleId, pageable);
    }

    @QueryMapping
    public Page<PerformanceEvaluation> getPerformanceEvaluations(@Argument Integer cycleId,
                                                                 @Argument int pageNo,
                                                                 @Argument int pageSize)
    {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return performanceService.getPerformanceEvaluations(cycleId, pageable);
    }

}