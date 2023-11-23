package com.hrms.careerpathmanagement.services;

import com.hrms.careerpathmanagement.dto.DiffPercentDTO;
import com.hrms.careerpathmanagement.dto.EmployeeGoalDTO;
import com.hrms.careerpathmanagement.dto.pagination.EmployeeGoalPagination;
import com.hrms.careerpathmanagement.models.Goal;
import com.hrms.careerpathmanagement.repositories.GoalRepository;
import com.hrms.careerpathmanagement.specification.CompetencySpecification;
import com.hrms.employeemanagement.projection.NameOnly;
import com.hrms.employeemanagement.projection.ProfileImageOnly;
import com.hrms.employeemanagement.repositories.EmployeeDamInfoRepository;
import com.hrms.employeemanagement.repositories.EmployeeRepository;
import com.hrms.employeemanagement.specification.EmployeeSpecification;
import com.hrms.global.dto.PieChartDTO;
import com.hrms.global.paging.PaginationSetup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GoalService {
    @PersistenceContext
    EntityManager em;
    private final GoalRepository goalRepository;
    private final EmployeeDamInfoRepository employeeDamInfoRepository;

    private final EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeSpecification employeeSpecification;
    @Autowired
    private CompetencySpecification competencySpecification;

    static final String PROFILE_IMAGE = "PROFILE_IMAGE";
    static final String STATUS_ONTRACK = "ONTRACK";
    static final String STATUS_COMPLETED = "COMPLETED";
    static final String STATUS_NOTSTART = "NOTSTART";
    static final String STATUS_TROUBLE = "TROUBLE";



    @Autowired
    public GoalService(GoalRepository goalRepository, EmployeeDamInfoRepository employeeDamInfoRepository,
                       EmployeeRepository employeeRepository) {
        this.goalRepository = goalRepository;
        this.employeeDamInfoRepository = employeeDamInfoRepository;
        this.employeeRepository = employeeRepository;
    }

    public EmployeeGoalPagination getEmployeesGoals(Integer departmentId, Integer cycleId,
                                           Integer pageNo, Integer pageSize)
    {
        var goals = getGoals(departmentId, cycleId, pageNo, pageSize);

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

    public DiffPercentDTO countGoals(Integer departmentId, Integer cycleId,
                                     Integer pageNo, Integer pageSize)
    {
        var goals = getGoals(departmentId, cycleId, pageNo, pageSize);
        var achievedCount = goals.stream().filter(i -> i.getStatus() == "Completed").count();
        return null;
    }

    public PieChartDTO getGoalsStatusStatistic(Integer departmentId, Integer cycleId)
    {
        var goals = goalRepository.findAllByDepartmentAndCycle(departmentId, cycleId);
        long totalGoals = goals.size();

        Map<String, Long> statusMap = goals.stream()
                .collect(Collectors.groupingBy(Goal::getStatus, Collectors.counting()));

        statusMap.entrySet().forEach(item -> log.info(item.getKey()));

        var pieChart = new PieChartDTO(new LinkedList(), new LinkedList());
        statusMap.entrySet().forEach(i -> {
            var status = i.getKey();
            var count = i.getValue();
            pieChart.getLabels().add(status);
            pieChart.getDatasets().add((float) (count / totalGoals));
        });

        return pieChart;
    }

    @NotNull
    private Page<Goal> getGoals(Integer departmentId, Integer cycleId, Integer pageNo, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        Specification<Goal> hasDepartmentSpec = employeeSpecification.hasDepartmentId(departmentId);
        Specification<Goal> hasCycleSpec = competencySpecification.hasCycleId(cycleId);
        return goalRepository.findAll(hasDepartmentSpec.and(hasCycleSpec), pageRequest);
    }


}
