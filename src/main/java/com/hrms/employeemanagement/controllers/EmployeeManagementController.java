package com.hrms.employeemanagement.controllers;

import com.hrms.employeemanagement.documents.EmployeeDocument;
import com.hrms.employeemanagement.dto.*;
import com.hrms.employeemanagement.models.*;
import com.hrms.employeemanagement.dto.EmployeePagingDTO;
import com.hrms.global.paging.PagingInfo;
import com.hrms.employeemanagement.repositories.DepartmentRepository;
import com.hrms.employeemanagement.repositories.JobLevelRepository;
import com.hrms.employeemanagement.repositories.PositionRepository;
import com.hrms.employeemanagement.services.*;
import com.hrms.damservice.DamService;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
//Controller
public class EmployeeManagementController {
    @Autowired
    EmployeeManagementService employeeManagementService;
    @Autowired
    DamService damService;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    JobLevelRepository jobLevelRepository;
    @Autowired
    PositionRepository positionRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @QueryMapping(name = "employees")
    public EmployeePagingDTO findAllEmployees(@Nullable @Argument List<Integer> departmentIds,
                                              @Nullable @Argument List<Integer> currentContracts,
                                              @Nullable @Argument Boolean status, @Nullable @Argument String name,
                                              @Argument PagingInfo pagingInfo) {
        return employeeManagementService.filterEmployees(departmentIds, currentContracts, status, name, pagingInfo);
    }

    @QueryMapping(name = "employee")
    public EmployeeDetailDTO findEmployeeById(@Argument int id) {
        return employeeManagementService.getEmployeeDetail(id);
    }

    @QueryMapping(name = "newEmployees")
    public List<Employee> findNewEmployees() {
        return employeeManagementService.getNewEmployees();
    }

    @QueryMapping(name = "currentHeadcounts")
    public HeadcountDTO getHeadcounts() {
        return employeeManagementService.getHeadcountsStatistic();
    }

    @QueryMapping(name = "headcountChart")
    public List<HeadcountChartDataDTO> getHeadcountChart() {
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

    @PostMapping("/dam/upload/{employeeId}")
    public ResponseEntity<String> uploadFile(@PathVariable Integer employeeId,
                                                               @RequestParam("file") MultipartFile file,
                                                               @RequestParam("type") String type) {
        try {
            employeeManagementService.uploadFile(file, employeeId, type);
            return ResponseEntity.ok("Upload successful.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Upload failed.");
        }
    }

    @QueryMapping(name = "searchEmployees")
    public List<EmployeeDocument> searchEmployees(@Argument String name) {
        return employeeManagementService.searchEmployees(name);
    }
//
//    @GetMapping("/dam/retrieve/{employeeId}")
//    public ResponseEntity<Resource> getEmployeeProfilePictureUrl(@PathVariable Integer employeeId) {
//        try {
//            String url = employeeManagementService.getEmployeeProfilePictureUrl(employeeId);
//
//            // Check if the URL is not empty or null
//            if (url != null && !url.isEmpty()) {
//                // Fetch the image data from the Cloudinary URL
//                byte[] imageBytes = IOUtils.toByteArray(new URL(url).openStream());
//
//                // Create a ByteArrayResource from the image data
//                ByteArrayResource resource = new ByteArrayResource(imageBytes);
//
//                // Set the response headers for image data
//                HttpHeaders headers = new HttpHeaders();
//                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//                headers.setContentLength(imageBytes.length);
//
//                // Set the Content-Disposition header for download with a suggested filename
//                headers.set("Content-Disposition", "attachment; filename=image.jpg");
//
//                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
//            } else {
//                // If the URL is empty or null, return a "not found" response
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }
}
