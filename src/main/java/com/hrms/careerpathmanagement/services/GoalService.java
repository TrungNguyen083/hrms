package com.hrms.careerpathmanagement.services;

import com.hrms.careerpathmanagement.dto.CountAndPercentDTO;
import com.hrms.careerpathmanagement.dto.EmployeeGoalDTO;
import com.hrms.careerpathmanagement.dto.pagination.EmployeeGoalPagination;
import com.hrms.careerpathmanagement.dto.pagination.GoalPagination;
import com.hrms.careerpathmanagement.models.Goal;
import com.hrms.careerpathmanagement.repositories.GoalRepository;
import com.hrms.careerpathmanagement.specification.CompetencySpecification;
import com.hrms.employeemanagement.projection.NameOnly;
import com.hrms.employeemanagement.projection.ProfileImageOnly;
import com.hrms.employeemanagement.repositories.EmployeeDamInfoRepository;
import com.hrms.employeemanagement.repositories.EmployeeRepository;
import com.hrms.employeemanagement.specification.EmployeeSpecification;
import com.hrms.global.dto.PieChartDTO;
import com.hrms.global.paging.Pagination;
import com.hrms.global.paging.PaginationSetup;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GoalService {
    private final GoalRepository goalRepository;
    private final EmployeeDamInfoRepository employeeDamInfoRepository;

    private final EmployeeRepository employeeRepository;

    private EmployeeSpecification employeeSpecification;
    private CompetencySpecification competencySpecification;

    static final String PROFILE_IMAGE = "PROFILE_IMAGE";
    static final String STATUS_ONTRACK = "ONTRACK";
    static final String STATUS_COMPLETED = "COMPLETED";
    static final String STATUS_NOTSTART = "NOTSTART";
    static final String STATUS_TROUBLE = "TROUBLE";



    @Autowired
    public GoalService(GoalRepository goalRepository, EmployeeDamInfoRepository employeeDamInfoRepository,
                       EmployeeRepository employeeRepository, EmployeeSpecification employeeSpecification,
                       CompetencySpecification competencySpecification)
    {
        this.goalRepository = goalRepository;
        this.employeeDamInfoRepository = employeeDamInfoRepository;
        this.employeeRepository = employeeRepository;
        this.employeeSpecification = employeeSpecification;
        this.competencySpecification = competencySpecification;
    }

    public EmployeeGoalPagination getEmployeesGoals(Integer departmentId, Integer cycleId,
                                           Integer pageNo, Integer pageSize)
    {
        var sort = Sort.by(Sort.Direction.DESC, "updatedAt");
        var goals = getGoals(departmentId, cycleId, pageNo, pageSize, sort);

        var employeeIds = goals.stream().map(g -> g.getEmployee().getId()).toList();
        var employees = employeeRepository.findAllByIdIn(employeeIds, NameOnly.class);

        var profileImages = employeeDamInfoRepository.findByEmployeeIdsSetAndFileType(employeeIds, PROFILE_IMAGE);

        var result = goals.stream().map(goal -> {
            var emp = employees.stream().filter(e -> e.id() == goal.getEmployee().getId()).findFirst().orElse(null);

            var profileImage = profileImages.stream()
                    .filter(i -> i.getEmployeeId() == emp.id())
                    .findFirst().orElse(new ProfileImageOnly(null, "default"))
                    .getUrl();

            return new EmployeeGoalDTO(goal.getId(), emp.id(), emp.firstName(), emp.lastName(), profileImage,
                    goal.getTitle(), goal.getDescription(), goal.getProgress());
        }).toList();

        var pagination = PaginationSetup.setupPaging(result.size(), pageNo, pageSize);

        return new EmployeeGoalPagination(result, pagination);
    }

    public CountAndPercentDTO countGoalsCompleted(Integer departmentId, Integer cycleId)
    {
        Specification<Goal> hasDepSpec = employeeSpecification.hasDepartmentId(departmentId);
        Specification<Goal> hasCycleSpec = competencySpecification.hasCycleId(cycleId);
        var totalGoals = goalRepository.count(hasDepSpec.and(hasCycleSpec));

        Specification<Goal> completedSpec = (root, query, cb) -> cb.equal(root.get("status"), STATUS_COMPLETED);
        var completedCount = goalRepository.count(hasDepSpec.and(hasCycleSpec.and(completedSpec)));

        var percentage = totalGoals == 0 ? null : (float) completedCount * 100 /totalGoals;
        return new CountAndPercentDTO(completedCount,  percentage);
    }

    public PieChartDTO getGoalsStatusStatistic(Integer departmentId, Integer cycleId)
    {
        var goals = goalRepository.findAllByDepartmentAndCycle(departmentId, cycleId);
        long totalGoals = goals.size();

        Map<String, Long> statusMap = goals.stream()
                .collect(Collectors.groupingBy(Goal::getStatus, Collectors.counting()));

        var pieChart = new PieChartDTO(new ArrayList(), new ArrayList());

        statusMap.entrySet().forEach(i -> {
            var status = i.getKey();
            var count = i.getValue();
            pieChart.getLabels().add(status);
            pieChart.getDatasets().add((float) count * 100 / totalGoals);
        });

        return pieChart;
    }

    @NotNull
    private Page<Goal> getGoals(Integer departmentId, Integer cycleId,
                                Integer pageNo, Integer pageSize,
                                Sort sort) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);
        Specification<Goal> hasDepartmentSpec = employeeSpecification.hasDepartmentId(departmentId);
        Specification<Goal> hasCycleSpec = competencySpecification.hasCycleId(cycleId);
        return goalRepository.findAll(hasDepartmentSpec.and(hasCycleSpec), pageRequest);
    }


    public GoalPagination getGoals(Integer employeeId, Integer pageNo, Integer pageSize) {
        Sort sort = Sort.by("updatedAt").descending();
        Pageable page = PageRequest.of(pageNo - 1, pageSize, sort);

        var goals = goalRepository.findAllByEmployeeId(employeeId, page);

        Pagination pagination = PaginationSetup.setupPaging(goals.getSize(), pageNo, pageSize);

        return new GoalPagination(goals.toList(), pagination);
    }

}
