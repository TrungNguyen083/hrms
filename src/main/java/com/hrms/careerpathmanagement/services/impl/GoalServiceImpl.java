package com.hrms.careerpathmanagement.services.impl;

import com.hrms.careerpathmanagement.dto.*;
import com.hrms.careerpathmanagement.models.Goal;
import com.hrms.careerpathmanagement.repositories.GoalRepository;
import com.hrms.careerpathmanagement.services.GoalService;
import com.hrms.employeemanagement.models.Employee;
import com.hrms.employeemanagement.projection.ProfileImageOnly;
import com.hrms.employeemanagement.repositories.EmployeeDamInfoRepository;
import com.hrms.employeemanagement.repositories.EmployeeRepository;
import com.hrms.employeemanagement.services.EmployeeManagementService;
import com.hrms.global.GlobalSpec;
import com.hrms.global.models.EvaluateCycle;
import com.hrms.performancemanagement.repositories.EvaluateCycleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GoalServiceImpl implements GoalService {
    private final GoalRepository goalRepository;
    private final EmployeeDamInfoRepository employeeDamInfoRepository;
    private final EvaluateCycleRepository evaluateCycleRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeManagementService employeeManagementService;
    static final String PROFILE_IMAGE = "PROFILE_IMAGE";
    static final String COMPLETED = "Completed";
    static final String UNCOMPLETED = "Uncompleted";



    @Autowired
    public GoalServiceImpl(GoalRepository goalRepository,
                           EmployeeDamInfoRepository employeeDamInfoRepository,
                           EmployeeRepository employeeRepository,
                           EmployeeManagementService employeeManagementService,
                           EvaluateCycleRepository evaluateCycleRepository)
    {
        this.goalRepository = goalRepository;
        this.employeeDamInfoRepository = employeeDamInfoRepository;
        this.employeeRepository = employeeRepository;
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

            return new GoalProgressDTO(goal.getEmployee().getId(), goal.getGoalName(), profileImage, (float) goal.getProgress());
        }).toList();
    }

    @Override
    public List<CompareGoal> getCompareGoals(List<Integer> employeeIds, Integer cycleId) {
        List<Employee> employees = employeeRepository.findAll(GlobalSpec.hasIds(employeeIds));
        EvaluateCycle evaluateCycle = evaluateCycleRepository.findById(cycleId).orElseThrow();

        Specification<Goal> hasEmployees = GlobalSpec.hasEmployeeIds(employeeIds);
        Specification<Goal> hasYear = GlobalSpec.hasYear(evaluateCycle.getYear());
        List<Goal> goals = goalRepository.findAll(hasEmployees.and(hasYear));

        return employees.stream().map(e -> {
            List<CompareGoalItem> filterGoals = goals.stream()
                    .filter(g -> g.getEmployee().getId().equals(e.getId()))
                    .map(g -> new CompareGoalItem(g.getGoalName(), g.getProgress()))
                    .toList();

            return new CompareGoal(e.getFirstName(), e.getLastName(), filterGoals);
        }).toList();
    }

    @Override
    public ChartData getCompareGoalPieChart(List<Integer> employeeIds, Integer cycleId) {
        List<Employee> employees = employeeRepository.findAll(GlobalSpec.hasIds(employeeIds));
        EvaluateCycle evaluateCycle = evaluateCycleRepository.findById(cycleId).orElseThrow();

        Specification<Goal> hasEmployees = GlobalSpec.hasEmployeeIds(employeeIds);
        Specification<Goal> hasYear = GlobalSpec.hasYear(evaluateCycle.getYear());
        List<Goal> goals = goalRepository.findAll(hasEmployees.and(hasYear));

        List<ChartItem> datasets = employees.stream().map(e -> {
            float eProgress = (float) goals.stream()
                    .filter(g -> g.getEmployee().getId().equals(e.getId()))
                    .mapToDouble(Goal::getProgress)
                    .average().orElse(0);

            return new ChartItem(e.getFullName(), List.of(eProgress, 100 - eProgress));
        }).toList();

        return new ChartData(List.of(COMPLETED, UNCOMPLETED), datasets);
    }
}
