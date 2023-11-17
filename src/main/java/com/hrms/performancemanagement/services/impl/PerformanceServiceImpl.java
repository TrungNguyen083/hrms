package com.hrms.performancemanagement.services.impl;

import com.hrms.careerpathmanagement.dto.EmployeePotentialPerformanceDTO;
import com.hrms.careerpathmanagement.models.PerformanceRange;
import com.hrms.careerpathmanagement.repositories.PerformanceEvaluationRepository;
import com.hrms.careerpathmanagement.repositories.PerformanceRangeRepository;
import com.hrms.employeemanagement.dto.EmployeeRatingDTO;
import com.hrms.employeemanagement.dto.EmployeeRatingPagination;
import com.hrms.employeemanagement.models.JobLevel;
import com.hrms.employeemanagement.repositories.JobLevelRepository;
import com.hrms.employeemanagement.services.EmployeeManagementService;
import com.hrms.employeemanagement.specification.EmployeeSpecification;
import com.hrms.global.dto.DataItemDTO;
import com.hrms.global.dto.DataItemPagingDTO;
import com.hrms.global.paging.Pagination;
import com.hrms.global.paging.PaginationSetup;
import com.hrms.performancemanagement.dto.DatasetDTO;
import com.hrms.performancemanagement.dto.PerformanceByJobLevalChartDTO;
import com.hrms.performancemanagement.model.PerformanceCycle;
import com.hrms.performancemanagement.model.PerformanceEvaluation;
import com.hrms.performancemanagement.repositories.PerformanceCycleRepository;
import com.hrms.performancemanagement.services.PerformanceService;
import com.hrms.performancemanagement.specification.PerformanceSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PerformanceServiceImpl implements PerformanceService {
    private final EmployeeManagementService employeeService;
    private final PerformanceEvaluationRepository performanceEvaluationRepository;
    private final PerformanceCycleRepository performanceCycleRepository;
    private final JobLevelRepository jobLevelRepository;
    private final PerformanceRangeRepository performanceRangeRepository;
    private final EmployeeSpecification employeeSpecification;
    private final PerformanceSpecification performanceSpecification;

    @Autowired
    public PerformanceServiceImpl(EmployeeManagementService employeeService,
                                  PerformanceEvaluationRepository performanceEvaluationRepository,
                                  PerformanceCycleRepository performanceCycleRepository,
                                  JobLevelRepository jobLevelRepository,
                                  PerformanceRangeRepository performanceRangeRepository,
                                  EmployeeSpecification employeeSpecification,
                                  PerformanceSpecification performanceSpecification)
    {
        this.employeeService = employeeService;
        this.performanceEvaluationRepository = performanceEvaluationRepository;
        this.performanceCycleRepository = performanceCycleRepository;
        this.jobLevelRepository = jobLevelRepository;
        this.performanceRangeRepository = performanceRangeRepository;
        this.employeeSpecification = employeeSpecification;
        this.performanceSpecification = performanceSpecification;
    }

    private Integer getLatestCycleId() {
        return performanceCycleRepository.findTopByOrderByPerformanceCycleIdDesc().orElse(0);
    }

    @Override
    public List<PerformanceCycle> getAllPerformanceCycles() {
        return performanceCycleRepository.findAll();
    }

    @Override
    public Page<PerformanceEvaluation> getPerformanceEvaluations(Integer empId, Pageable pageable) {
        Specification<PerformanceEvaluation> spec = employeeSpecification.hasEmployeeId(empId);
        return performanceEvaluationRepository.findAll(spec, pageable);
    }

    public List<PerformanceEvaluation> getLatestEvaluations(Integer departmentId) {
        Specification<PerformanceEvaluation> departmentSpec = employeeSpecification.hasDepartmentId(departmentId);
        Specification<PerformanceEvaluation> cycleSpec = performanceSpecification.hasPerformanceCycleId(getLatestCycleId());

        return performanceEvaluationRepository.findAll(departmentSpec.and(cycleSpec));
    }

    public List<PerformanceEvaluation> getEvaluations(Integer positionId, Integer performanceCycleId) {
        Specification<PerformanceEvaluation> positionFilter = employeeSpecification.hasPositionId(positionId);
        Specification<PerformanceEvaluation> cycleFilter = performanceSpecification.hasPerformanceCycleId(performanceCycleId);

        return performanceEvaluationRepository.findAll(positionFilter.and(cycleFilter));
    }

    @Override
    public PerformanceByJobLevalChartDTO getPerformanceByJobLevel(Integer positionId, Integer cycleId) {
        var evaluations = performanceEvaluationRepository.findByCycleIdAndPositionId(positionId, cycleId);
        var performanceRanges = performanceRangeRepository.findAll();
        var labels = jobLevelRepository.findAll();
        var datasets = createDatasets(evaluations, performanceRanges, labels);

        return new PerformanceByJobLevalChartDTO(labels, datasets);
    }

    @Override
    public List<EmployeePotentialPerformanceDTO> getPotentialAndPerformance(Integer departmentId, Integer cycleId) {
        Specification<PerformanceEvaluation> empSpec = employeeSpecification.hasDepartmentId(departmentId);
        Specification<PerformanceEvaluation> cycleSpec = performanceSpecification.hasPerformanceCycleId(cycleId);

        var evaluations = performanceEvaluationRepository.findAll(empSpec.and(cycleSpec));
        evaluations.forEach(e -> log.info(e.getEmployee().getFullName()));

        List<EmployeePotentialPerformanceDTO> results = new ArrayList<>();

        evaluations.forEach(item -> results.add(new EmployeePotentialPerformanceDTO(
                item.getEmployee().getFullName(),
                item.getEmployee().getProfileBio(),
                item.getPotentialScore(),
                item.getFinalAssessment()
        )));
        return results;
    }

    @Override
    public EmployeeRatingPagination getPerformanceRating(Integer cycleId, PageRequest pageable) {
        Specification<PerformanceEvaluation> cycleSpec = performanceSpecification.hasPerformanceCycleId(cycleId);
        var evaluations = performanceEvaluationRepository.findAll(cycleSpec, pageable);

        List<EmployeeRatingDTO> results = new ArrayList<>();

        evaluations.forEach(item -> results.add(new EmployeeRatingDTO(
                item.getEmployee().getId(),
                item.getEmployee().getFirstName(),
                item.getEmployee().getLastName(),
                employeeService.getProfilePicture(item.getEmployee().getId()),
                item.getFinalAssessment()
        )));

        Pagination pagination = PaginationSetup.setupPaging(evaluations.getTotalElements(), pageable.getPageNumber(), pageable.getPageSize());
        return new EmployeeRatingPagination(results, pagination);
    }

    private List<DatasetDTO> createDatasets(List<PerformanceEvaluation> evaluations,
                                            List<PerformanceRange> performanceRanges,
                                            List<JobLevel> labels)
    {
        List<DatasetDTO> datasetDTOS = new ArrayList<>();

        performanceRanges.forEach(range -> {
            var dataset = createDatasetForRange(evaluations, range, labels);
            datasetDTOS.add(dataset);
        });

        return datasetDTOS;
    }

    private DatasetDTO createDatasetForRange(List<PerformanceEvaluation> evaluations,
                                             PerformanceRange range,
                                             List<JobLevel> labels)
    {
        DatasetDTO datasetDTO = new DatasetDTO(range.getText(), new ArrayList<>());
        labels.forEach( label -> {
            long countEvalsInPositionLevel = countEvals(label, evaluations);
            long count = countPerformancesInRange(label, range, evaluations);
            float percentage = calculatePercentage(count, countEvalsInPositionLevel);
            datasetDTO.getData().add(percentage);
        });
        return datasetDTO;
    }

    private long countEvals(JobLevel jobLevel, List<PerformanceEvaluation> evaluations) {
        return evaluations.stream()
                .filter(e -> e.getEmployee().getJobLevel().getId() == jobLevel.getId())
                .count();
    }

    private long countPerformancesInRange(JobLevel jobLevel, PerformanceRange range, List<PerformanceEvaluation> evaluations) {
        return evaluations.stream()
                .filter(eval -> eval.getEmployee().getJobLevel().getId() == jobLevel.getId())
                .filter(eval -> eval.getFinalAssessment() >= range.getMinValue())
                .filter(eval -> eval.getFinalAssessment() <= range.getMaxValue())
                .count();
    }

    private float calculatePercentage(long amount, long total) {
        return total == 0 ? 0 : (float) (amount * 100) / total;
    }



    @Override
    public DataItemPagingDTO getEmployeePerformanceRatingScore(Integer employeeId,
                                                                     Integer pageNo, Integer pageSize) {
        performanceCycleRepository.findAll();

        Specification<PerformanceEvaluation> spec = employeeSpecification.hasEmployeeId(employeeId);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<PerformanceEvaluation> page = performanceEvaluationRepository.findAll(spec, pageable);
        List<DataItemDTO> data = page.map(item -> new DataItemDTO(
                item.getPerformanceCycle().getPerformanceCycleName(),
                item.getFinalAssessment()
        )).getContent();

        Pagination pagination = PaginationSetup.setupPaging(page.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize());

        return new DataItemPagingDTO(data, pagination);
    }

}