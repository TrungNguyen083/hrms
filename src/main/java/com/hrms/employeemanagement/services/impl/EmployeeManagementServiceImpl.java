package com.hrms.employeemanagement.services.impl;

import com.hrms.damservice.DamService;
import com.hrms.employeemanagement.dto.*;
import com.hrms.employeemanagement.models.*;
import com.hrms.employeemanagement.specification.EmployeeDamInfoSpec;
import com.hrms.global.paging.Pagination;
import com.hrms.global.paging.PaginationSetup;
import com.hrms.global.paging.PagingInfo;
import com.hrms.employeemanagement.repositories.*;
import com.hrms.employeemanagement.services.EmployeeManagementService;
import com.unboundid.util.NotNull;
import com.unboundid.util.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;



@Service
@Transactional
public class EmployeeManagementServiceImpl implements EmployeeManagementService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private EmergencyContactRepository emergencyContactRepository;
    @Autowired
    private EmployeeDamInfoRepository employeeDamInfoRepository;
    @Autowired
    private DamService damService;
    private ModelMapper modelMapper;

    @Bean
    public void setUpMapper() {
        this.modelMapper = new ModelMapper();
    }

    @Override
    public List<Employee> getAllEmployees() {
        //Find all employee have status not equal "Terminated"
        Specification<Employee> spec = (root, query, builder) -> builder.notEqual(root.get("status"), 0);
        return employeeRepository.findAll(spec);
    }

    @Override
    public Employee findEmployee(Integer id) {
        Specification<Employee> spec = ((root, query, builder) -> builder.equal(root.get("id"), id));
        return employeeRepository
                .findOne(spec)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    @Override
    public EmployeeDetailDTO getEmployeeDetail(Integer id) {
        Specification<Employee> spec = ((root, query, builder) -> builder.equal(root.get("id"), id));
        Employee employee = employeeRepository
                .findOne(spec)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        Specification<EmergencyContact> contactSpec = (root, query, builder)
                -> builder.equal(root.get("employee").get("id"), id);
        List<EmergencyContact> emergencyContacts = emergencyContactRepository.findAll(contactSpec);
        //Get employeeDamInfo have employeeId = id and type = "Profile Picture" and has the latest uploadedAt
        String imageUrl = employeeDamInfoRepository
                .findAll(EmployeeDamInfoSpec.hasEmployeeAndType(id, "Profile Picture"))
                .stream()
                .max(Comparator.comparing(EmployeeDamInfo::getUploadedAt)).map(EmployeeDamInfo::getUrl)
                .orElse(null);
        return new EmployeeDetailDTO(employee, emergencyContacts, imageUrl);
    }

    @Override
    public List<Employee> findEmployees(List<Integer> departmentIds) {
        Specification<Employee> spec = (root, query, criteriaBuilder) -> root.get("department").get("id").in(departmentIds);
        return employeeRepository.findAll(spec);
    }

    @Override
    public List<Employee> findEmployees(Integer departmentId) {
        //have departmentId = departmentId and status not equal to 0
        Specification<Employee> spec = (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("department").get("id"), departmentId),
                criteriaBuilder.notEqual(root.get("status"), 0)
        );
        return employeeRepository.findAll(spec);
    }

    @Override
    public List<Employee> getNewEmployees() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("joinedDate")));

        return employeeRepository.findAll(pageRequest).getContent();
    }

    @Override
    public EmployeePagingDTO filterEmployees(@Nullable List<Integer> departmentIds,
                                             @Nullable List<Integer> currentContracts,
                                             @Nullable Boolean status,
                                             @Nullable String name,
                                             PagingInfo pagingInfo) {
        Sort sort = pagingInfo.getSortBy() != null ? Sort.by(Sort.Direction.DESC, pagingInfo.getSortBy()) : null;
        Pageable pageable = sort != null
                ? PageRequest.of(pagingInfo.getPageNo() - 1, pagingInfo.getPageSize(), sort)
                : PageRequest.of(pagingInfo.getPageNo() - 1, pagingInfo.getPageSize());
        Specification<Employee> filterSpec = (root, query, criteriaBuilder) -> criteriaBuilder.and(
                departmentIds != null && !departmentIds.isEmpty()
                        ? root.get("department").get("id").in(departmentIds)
                        : criteriaBuilder.conjunction(),
                currentContracts != null && !currentContracts.isEmpty()
                        ? root.get("currentContract").in(currentContracts)
                        : criteriaBuilder.conjunction(),
                status != null
                        ? criteriaBuilder.equal(root.get("user").get("isEnabled"), status)
                        : criteriaBuilder.conjunction(),
                name != null
                        ? criteriaBuilder.or(
                        criteriaBuilder.like(root.get("lastName"), "%" + name + "%"),
                        criteriaBuilder.like(root.get("firstName"), "%" + name + "%"))
                        : criteriaBuilder.conjunction()
        );

        Page<EmployeeDTO> empPage = employeeRepository.findAll(filterSpec, pageable).map(employee -> {
            String imageUrl = employeeDamInfoRepository
                    .findAll(EmployeeDamInfoSpec.hasEmployeeAndType(employee.getId(), "Profile Picture"))
                    .stream()
                    .max(Comparator.comparing(EmployeeDamInfo::getUploadedAt)).map(EmployeeDamInfo::getUrl)
                    .orElse(null);
            return new EmployeeDTO(employee, imageUrl);
        });

        Pagination pagination = PaginationSetup.setupPaging(empPage.getTotalElements(), pagingInfo.getPageNo(), pagingInfo.getPageSize());
        return new EmployeePagingDTO(empPage.getContent(), pagination);
    }

    @Override
    public HeadcountDTO getHeadcountsStatistic() {
        //Get all new employees have joinedDate between 2 years ago and 1 year ago
        LocalDate datePrevious = LocalDate.now().minusYears(1);
        var countPreviousYearEmployees = countEmployeesByYear(datePrevious);

        //Get all new employees have joinedDate between today and 1 year ago
        LocalDate dateCurrent = LocalDate.now();
        var countCurrentYearEmployees = countEmployeesByYear(dateCurrent);

        var countAllEmployee = getAllEmployees().size();

        float diffPercent = ((float) (countCurrentYearEmployees - countPreviousYearEmployees) / countPreviousYearEmployees) * 100;

        return new HeadcountDTO(countAllEmployee, diffPercent, countPreviousYearEmployees <= countCurrentYearEmployees);
    }

    @Override
    public List<HeadcountChartDataDTO> getHeadcountChartData() {
        List<Department> department = departmentRepository.findAll();
        List<Integer> departmentIds = department.stream().map(Department::getId).toList();
        //Find all employees in departmentIds and have status not equal to 0
        Specification<Employee> spec = (root, query, builder) -> builder.and(
                builder.in(root.get("department").get("id")).value(departmentIds),
                builder.notEqual(root.get("status"), 0)
        );
        List<Employee> employees = employeeRepository.findAll(spec);

        return department.stream().map(item -> {
            Integer countEmployee = Math.toIntExact(employees
                    .stream()
                    .filter(employee -> employee.getDepartment().getId() == item.getId())
                    .count());
            return new HeadcountChartDataDTO(item.getDepartmentName(), countEmployee);
        }).toList();
    }

    private long countEmployeesByYear(LocalDate date) {
        //Get all new employees have joinedDate before date and have status not equal to 0
        Specification<Employee> spec = (root, query, builder) -> builder.and(
                builder.lessThan(root.get("joinedDate"), date),
                builder.notEqual(root.get("status"), 0)
        );

        return employeeRepository.count(spec);
    }

    @Override
    @Transactional
    public Employee createEmployee(EmployeeInputDTO employeeInputDTO) {
        Employee employee = new Employee();

        return updateEmployee(employeeInputDTO, employee);
    }

    @Override
    @Transactional
    public Employee updateEmployee(EmployeeInputDTO input) {
        Employee employee = findEmployee(input.getId());

        return updateEmployee(input, employee);
    }

    @NotNull
    private Employee updateEmployee(EmployeeInputDTO employeeInputDTO, Employee employee) {
        modelMapper.map(employeeInputDTO, employee);
        employeeRepository.save(employee);
        manageEmergencyContacts(employeeInputDTO.getEmergencyContacts(), employee);

        return employee;
    }

    private void manageEmergencyContacts(List<EmergencyContactInputDTO> emergencyContacts, Employee employee) {
        deleteEmerContactNotInNewList(emergencyContacts, employee.getId());
        insertOrUpdateEmerContact(emergencyContacts, employee);
    }

    private void deleteEmerContactNotInNewList(List<EmergencyContactInputDTO> emergencyContacts, Integer employeeId) {
        // Get all emergency contacts of the employee
        Specification<EmergencyContact> spec = (root, query, builder) -> builder.equal(root.get("employee").get("id"), employeeId);
        List<EmergencyContact> ecs = emergencyContactRepository.findAll(spec);

        // Collect emergency contacts to be deleted
        List<EmergencyContact> ecsToDelete = ecs.stream()
                .filter(ec -> emergencyContacts.stream()
                        .noneMatch(e -> e.getId() != null && e.getId().equals(ec.getId())))
                .toList();

        // Delete the collected emergency contacts
        emergencyContactRepository.deleteAll(ecsToDelete);
    }

    private void insertOrUpdateEmerContact(List<EmergencyContactInputDTO> emergencyContacts, Employee employee) {
        List<Integer> ecIds = emergencyContacts.stream()
                .map(EmergencyContactInputDTO::getId)
                .toList();
        Specification<EmergencyContact> spec = (root, query, builder) -> root.get("id").in(ecIds);
        List<EmergencyContact> ecs = emergencyContactRepository.findAll(spec);

        List<EmergencyContact> emergencyContactList = emergencyContacts.stream()
                .map(ec -> {
                    EmergencyContact emergencyContact = ec.getId() == null ? new EmergencyContact() :
                            ecs.stream()
                                    .filter(e -> e.getId() == ec.getId())
                                    .findFirst()
                                    .orElseThrow(() -> new RuntimeException("Emergency Contact not found with id: " + ec.getId()));
                    modelMapper.map(ec, emergencyContact);
                    emergencyContact.setEmployee(employee);
                    return emergencyContact;
                })
                .toList();

        emergencyContactRepository.saveAll(emergencyContactList);
    }

    @Override
    public void uploadFile(MultipartFile file, Integer employeeId, String type) throws IOException {
        // Upload the image using the DamService with the original file name
        Map uploadResult = damService.uploadFile(file);
        // Get the file's extension like jpg, png, docx, ...
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String url = uploadResult.get("url").toString();
        // Update the employee's profile picture public ID in the database
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new RuntimeException("Employee not found with id: " + employeeId));
        EmployeeDamInfo employeeDam = EmployeeDamInfo.builder()
                .employee(employee)
                .fileName(file.getOriginalFilename())
                .type(type)
                .extension(extension)
                .url(url)
                .uploadedAt(new Date(System.currentTimeMillis()))
                .build();
        employeeDamInfoRepository.save(employeeDam);
    }

    @Override
    public String getQualifications(Integer employeeId) {
        return null;
    }

}
