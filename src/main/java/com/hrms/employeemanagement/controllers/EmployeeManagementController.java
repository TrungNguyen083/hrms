package com.hrms.employeemanagement.controllers;

import com.hrms.careerpathmanagement.dto.PercentageChangeDTO;
import com.hrms.digitalassetmanagement.services.DamService;
import com.hrms.employeemanagement.dto.*;
import com.hrms.employeemanagement.models.*;
import com.hrms.employeemanagement.dto.pagination.EmployeePagingDTO;
import com.hrms.employeemanagement.projection.ProfileImageOnly;
import com.hrms.global.dto.BarChartDTO;
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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('PM') or hasAuthority('EMPLOYEE')")
    public EmployeeOverviewDTO getEmployeeOverview(@Argument Integer employeeId) {
        return employeeManagementService.getProfileOverview(employeeId);
    }

    @QueryMapping(name = "employeeId")
    @PreAuthorize("hasAuthority('PM') or hasAuthority('EMPLOYEE') or hasAuthority('HR') or hasAuthority('ADMIN')")
    public Integer getEmployeeIdByEmail(@Argument String email) {
        return employeeManagementService.getEmployeeIdByEmail(email);
    }

    @QueryMapping(name = "profileImage")
    @PreAuthorize("hasAuthority('PM') or hasAuthority('EMPLOYEE') or hasAuthority('HR') or hasAuthority('ADMIN')")
    public String getProfileImageByEmail(@Argument String email) {
        return employeeManagementService.getProfileImageByEmail(email);
    }

    @QueryMapping(name = "employee")
    @PreAuthorize("hasAuthority('PM') or hasAuthority('EMPLOYEE') or hasAuthority('HR')")
    public EmployeeDTO findEmployeeById(@Argument int id) {
        return employeeManagementService.getEmployeeDetail(id);
    }

    @QueryMapping(name = "newEmployees")
    @PreAuthorize("hasAuthority('PM') or hasAuthority('EMPLOYEE') or hasAuthority('HR')")
    public List<EmployeeDTO> findNewEmployees() {
        return employeeManagementService.getNewEmployees();
    }

    @QueryMapping(name = "currentHeadcounts")
    @PreAuthorize("hasAuthority('SUM') or hasAuthority('HR')")
    public PercentageChangeDTO getHeadcounts(@Argument Integer cycleId) {
        return employeeManagementService.getHeadcountsStatistic(cycleId);
    }

    @QueryMapping(name = "headcountChart")
    @PreAuthorize("hasAuthority('SUM') or hasAuthority('HR')")
    public BarChartDTO getHeadcountChart() {
        return employeeManagementService.getHeadcountChartData();
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('HR')")
    public Employee createProfile(@Argument EmployeeInputDTO input) {
        return employeeManagementService.createEmployee(input);
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('PM') or hasAuthority('EMPLOYEE') or hasAuthority('HR')")
    public Employee updateEmployee(@Argument EmployeeInputDTO input) {
        return employeeManagementService.updateEmployee(input);
    }



    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/dam/upload/{employeeId}")
    @PreAuthorize("hasAuthority('PM') or hasAuthority('EMPLOYEE') or hasAuthority('HR')")
    public ResponseEntity<String> uploadFile(@PathVariable Integer employeeId,
                                             @RequestParam("title") @Nullable String title,
                                             @RequestParam("file") MultipartFile file,
                                             @RequestParam("type") String type) {
        try {
            employeeManagementService.uploadPersonalFile(file, employeeId, type, title);
            return ResponseEntity.ok("Upload successful.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/dam/retrieve/{employeeId}")
    @PreAuthorize("hasAuthority('PM') or hasAuthority('EMPLOYEE') or hasAuthority('HR')")
    public ResponseEntity<String> getEmployeeProfilePictureUrl(@PathVariable Integer employeeId) {
        String url = employeeManagementService.getProfilePicture(employeeId);
        return ResponseEntity.ok(url);
    }

    @QueryMapping(name = "departmentEmployees")
    @PreAuthorize("hasAuthority('PM') or hasAuthority('EMPLOYEE') or hasAuthority('HR')")
    public List<SimpleItemDTO> getDepartmentEmployees(@Argument Integer departmentId, @Argument Integer positionId) {
        return employeeManagementService.getDepartmentEmployees(departmentId, positionId);
    }
      
    @GetMapping("/dam/profile-images")
    @PreAuthorize("hasAuthority('PM') or hasAuthority('EMPLOYEE') or hasAuthority('HR')")
    public ResponseEntity<List<ProfileImageOnly>> getEmployeeProfileImg(@RequestParam List<Integer> employeeIds) {
        return ResponseEntity.ok(employeeManagementService.getEmployeesNameAndAvatar(employeeIds));
    }

    @QueryMapping(name="qualifications")
    @PreAuthorize("hasAuthority('PM') or hasAuthority('EMPLOYEE')")
    public List<QualificationDTO> getQualifications(@Argument Integer employeeId) {
        return employeeManagementService.getQualifications(employeeId);
    }

    @QueryMapping(name = "employeesInDepartment")
    @PreAuthorize("hasAuthority('PM') or hasAuthority('EMPLOYEE') or hasAuthority('HR')")
    public List<NameImageDTO> getEmployeesInDepartment(@Argument Integer departmentId) {
        return employeeManagementService.getNameImagesInDepartment(departmentId);
    }

    @QueryMapping(name = "departmentHeadcount")
    @PreAuthorize("hasAuthority('PM') or hasAuthority('EMPLOYEE') or hasAuthority('HR')")
    public PercentageChangeDTO getDepartmentHeadcount(@Argument Integer departmentId) {
        return employeeManagementService.getDepartmentHeadcount(departmentId);
    }

    @QueryMapping(name = "departmentHeadcountChart")
    @PreAuthorize("hasAuthority('PM') or hasAuthority('EMPLOYEE') or hasAuthority('HR')")
    public BarChartDTO getDepartmentHeadcountChart(@Argument Integer departmentId) {
        return employeeManagementService.getDepartmentHeadcountChart(departmentId);
    }
}
