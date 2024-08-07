package com.hrms.employeemanagement.controllers;

import com.hrms.careerpathmanagement.dto.PercentageChangeDTO;
import com.hrms.digitalassetmanagement.service.DamService;
import com.hrms.employeemanagement.dto.*;
import com.hrms.employeemanagement.models.*;
import com.hrms.employeemanagement.dto.pagination.EmployeePagingDTO;
import com.hrms.employeemanagement.projection.ProfileImageOnly;
import com.hrms.global.dto.BarChartDTO;
import com.hrms.global.models.Department;
import com.hrms.global.models.JobLevel;
import com.hrms.global.models.Position;
import com.hrms.employeemanagement.repositories.DepartmentRepository;
import com.hrms.employeemanagement.repositories.JobLevelRepository;
import com.hrms.employeemanagement.repositories.PositionRepository;
import com.hrms.employeemanagement.services.*;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class EmployeeManagementController {
    EmployeeManagementService employeeManagementService;
    DamService damService;
    DepartmentRepository departmentRepository;
    JobLevelRepository jobLevelRepository;
    PositionRepository positionRepository;

    @Autowired
    public EmployeeManagementController(EmployeeManagementService employeeManagementService, DamService damService,
                                        DepartmentRepository departmentRepository, JobLevelRepository jobLevelRepository,
                                        PositionRepository positionRepository) {
        this.employeeManagementService = employeeManagementService;
        this.damService = damService;
        this.departmentRepository = departmentRepository;
        this.jobLevelRepository = jobLevelRepository;
        this.positionRepository = positionRepository;
    }

    @QueryMapping(name = "employees")
    public EmployeePagingDTO findAllEmployees(@Nullable @Argument List<Integer> departmentIds,
                                              @Nullable @Argument List<Integer> currentContracts,
                                              @Nullable @Argument Boolean status, @Nullable @Argument String name,
                                              @Argument Integer pageNo, @Argument Integer pageSize) {
        return employeeManagementService.filterEmployees(departmentIds, currentContracts, status, name, pageNo, pageSize);
    }

    @QueryMapping(name = "employeeOverview")
    public EmployeeOverviewDTO getEmployeeOverview(@Argument Integer employeeId) {
        return employeeManagementService.getProfileOverview(employeeId);
    }

    @QueryMapping(name = "employee")
    public EmployeeDTO findEmployeeById(@Argument int id) {
        return employeeManagementService.getEmployeeDetail(id);
    }

    @QueryMapping(name = "newEmployees")
    public List<EmployeeDTO> findNewEmployees() {
        return employeeManagementService.getNewEmployees();
    }

    @QueryMapping(name = "currentHeadcounts")
    public PercentageChangeDTO getHeadcounts() {
        return employeeManagementService.getHeadcountsStatistic();
    }

    @QueryMapping(name = "headcountChart")
    public BarChartDTO getHeadcountChart() {
        return employeeManagementService.getHeadcountChartData();
    }

    @MutationMapping
    public Employee createProfile(@Argument EmployeeInputDTO input) throws Exception {
        return employeeManagementService.createEmployee(input);
    }

    @MutationMapping
    public Employee updateEmployee(@Argument EmployeeInputDTO input) {
        return employeeManagementService.updateEmployee(input);
    }

    @QueryMapping(name = "departments")
    public List<Department> getDepartments() {
        return departmentRepository.findAll();
    }

    @QueryMapping(name = "NumberOfDepartments")
    public Long getNumberOfDepartments() {
        return departmentRepository.count();
    }

    @QueryMapping(name = "jobLevels")
    public List<JobLevel> getJobLevels() {
        return jobLevelRepository.findAll();
    }

    @QueryMapping(name = "positions")
    public List<Position> getPositions() {
        return positionRepository.findAll();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/dam/upload/{employeeId}")
    public ResponseEntity<String> uploadFile(@PathVariable Integer employeeId,
                                             @RequestParam("file") MultipartFile file,
                                             @RequestParam("type") String type) {
        try {
            employeeManagementService.uploadPersonalFile(file, employeeId, type);
            return ResponseEntity.ok("Upload successful.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/dam/retrieve/{employeeId}")
    public ResponseEntity<String> getEmployeeProfilePictureUrl(@PathVariable Integer employeeId) {
        String url = employeeManagementService.getProfilePicture(employeeId);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/qualifications/{employeeId}/{type}")
    public ResponseEntity<List<EmployeeDamInfoDTO>> getEmployeeQualifications(@PathVariable Integer employeeId,
                                                                              @PathVariable String type) {
        return ResponseEntity.ok(employeeManagementService.getQualifications(employeeId));
    }

    @QueryMapping(name = "departmentEmployees")
    public List<SimpleItemDTO> getDepartmentEmployees(@Argument Integer departmentId, @Argument Integer positionId) {
        return employeeManagementService.getDepartmentEmployees(departmentId, positionId);
    }
      
    @GetMapping("/dam/profile-images")
    public ResponseEntity<List<ProfileImageOnly>> getEmployeeProfileImg(@RequestParam List<Integer> employeeIds) {
        return ResponseEntity.ok(employeeManagementService.getEmployeesNameAndAvatar(employeeIds));
    }

    @QueryMapping(name = "employeesInDepartment")
    public List<NameImageDTO> getEmployeesInDepartment(@Argument Integer departmentId) {
        return employeeManagementService.getNameImagesInDepartment(departmentId);
    }

    @QueryMapping(name = "departmentHeadcount")
    public PercentageChangeDTO getDepartmentHeadcount(@Argument Integer departmentId) {
        return employeeManagementService.getDepartmentHeadcount(departmentId);
    }

    @QueryMapping(name = "departmentHeadcountChart")
    public BarChartDTO getDepartmentHeadcountChart(@Argument Integer departmentId) {
        return employeeManagementService.getDepartmentHeadcountChart(departmentId);
    }
}
