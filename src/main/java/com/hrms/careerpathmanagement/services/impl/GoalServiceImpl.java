package com.hrms.careerpathmanagement.services.impl;

import com.hrms.careerpathmanagement.dto.CountAndPercentDTO;
import com.hrms.careerpathmanagement.dto.EmployeeGoalDTO;
import com.hrms.careerpathmanagement.dto.GoalProgressDTO;
import com.hrms.careerpathmanagement.dto.pagination.EmployeeGoalPagination;
import com.hrms.careerpathmanagement.dto.pagination.GoalPagination;
import com.hrms.careerpathmanagement.models.Goal;
import com.hrms.careerpathmanagement.repositories.GoalRepository;
import com.hrms.careerpathmanagement.services.GoalService;
import com.hrms.employeemanagement.models.Employee;
import com.hrms.employeemanagement.projection.NameOnly;
import com.hrms.employeemanagement.projection.ProfileImageOnly;
import com.hrms.employeemanagement.repositories.EmployeeDamInfoRepository;
import com.hrms.employeemanagement.repositories.EmployeeRepository;
import com.hrms.employeemanagement.services.EmployeeManagementService;
import com.hrms.global.GlobalSpec;
import com.hrms.global.dto.PieChartDTO;
import com.hrms.global.models.EvaluateCycle;
import com.hrms.global.paging.PaginationSetup;
import com.hrms.performancemanagement.repositories.EvaluateCycleRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class GoalServiceImpl implements GoalService {
    private final GoalRepository goalRepository;
    private final EmployeeDamInfoRepository employeeDamInfoRepository;
    private final EvaluateCycleRepository evaluateCycleRepository;
    private final EmployeeManagementService employeeManagementService;
    static final String PROFILE_IMAGE = "PROFILE_IMAGE";



    @Autowired
    public GoalServiceImpl(GoalRepository goalRepository, EmployeeDamInfoRepository employeeDamInfoRepository,
                           EmployeeManagementService employeeManagementService,
                           EvaluateCycleRepository evaluateCycleRepository)
    {
        this.goalRepository = goalRepository;
        this.employeeDamInfoRepository = employeeDamInfoRepository;
        this.evaluateCycleRepository = evaluateCycleRepository;
        this.employeeManagementService = employeeManagementService;
    }

    public List<GoalProgressDTO> getDepartmentGoalProgress(Integer departmentId, Integer cycleId) {
        List<Integer> empIdSet = employeeManagementService.getEmployeesInDepartment(departmentId)
                .stream()
                .map(Employee::getId)
                .toList();

        List<ProfileImageOnly> profileImages = employeeDamInfoRepository.
                findByEmployeeIdsSetAndFileType(empIdSet, PROFILE_IMAGE);

        EvaluateCycle evaluateCycle = evaluateCycleRepository.findById(cycleId).orElseThrow();

        Specification<Goal> hasEmployeeIds = GlobalSpec.hasEmployeeIds(empIdSet);
        Specification<Goal> hasYear = GlobalSpec.hasYear(evaluateCycle.getYear());
        List<Goal> goals = goalRepository.findAll(hasEmployeeIds.and(hasYear));

        return goals.stream().map(goal -> {
            String profileImage = profileImages.stream()
                    .filter(profile -> profile.getEmployeeId().equals(goal.getEmployee().getId()))
                    .map(ProfileImageOnly::getUrl)
                    .findFirst()
                    .orElse(null);

            return new GoalProgressDTO(goal.getEmployee().getId(), goal.getGoalName(), profileImage, goal.getProgress());
        }).toList();
    }

    public CountAndPercentDTO countGoalsCompleted(Integer departmentId, Integer cycleId)
    {
//        Specification<Goal> hasDepSpec = employeeSpecification.hasDepartmentId(departmentId);
//        Specification<Goal> hasCycleSpec = GlobalSpec.hasEvaluateCycleId(cycleId);
//        var totalGoals = goalRepository.count(hasDepSpec.and(hasCycleSpec));
//
//        Specification<Goal> completedSpec = (root, query, cb) -> cb.equal(root.get("status"), STATUS_COMPLETED);
//        var completedCount = goalRepository.count(hasDepSpec.and(hasCycleSpec.and(completedSpec)));
//
//        var percentage = totalGoals == 0 ? null : (float) completedCount * 100 /totalGoals;
//        return new CountAndPercentDTO(completedCount,  percentage);

        return null;
    }

    public PieChartDTO getGoalsStatusStatistic(Integer departmentId, Integer cycleId)
    {
//        var goals = goalRepository.findAllByDepartmentAndCycle(departmentId, cycleId);
//        long totalGoals = goals.size();
//
//        Map<String, Long> statusMap = goals.stream()
//                .collect(Collectors.groupingBy(Goal::getStatus, Collectors.counting()));
//
//        var pieChart = new PieChartDTO(new ArrayList(), new ArrayList());
//
//        statusMap.entrySet().forEach(i -> {
//            var status = i.getKey();
//            var count = i.getValue();
//            pieChart.getLabels().add(status);
//            pieChart.getDatasets().add((float) count * 100 / totalGoals);
//        });
//
//        return pieChart;
        return null;
    }
}
