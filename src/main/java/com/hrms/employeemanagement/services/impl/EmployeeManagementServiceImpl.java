package com.hrms.employeemanagement.services.impl;

import com.hrms.careerpathmanagement.dto.PercentageChangeDTO;
import com.hrms.global.models.*;
import com.hrms.careerpathmanagement.models.SkillEvaluation;
import com.hrms.careerpathmanagement.repositories.PositionLevelSkillRepository;
import com.hrms.careerpathmanagement.repositories.SkillEvaluationRepository;
import com.hrms.careerpathmanagement.specification.CareerSpecification;
import com.hrms.digitalassetmanagement.service.DamService;
import com.hrms.employeemanagement.dto.*;
import com.hrms.employeemanagement.dto.pagination.EmployeePagingDTO;
import com.hrms.employeemanagement.models.*;
import com.hrms.employeemanagement.projection.ProfileImageOnly;
import com.hrms.employeemanagement.specification.EmployeeDamInfoSpec;
import com.hrms.global.GlobalSpec;
import com.hrms.global.dto.BarChartDTO;
import com.hrms.global.dto.DataItemDTO;
import com.hrms.global.paging.Pagination;
import com.hrms.global.paging.PaginationSetup;
import com.hrms.employeemanagement.repositories.*;
import com.hrms.employeemanagement.services.EmployeeManagementService;
import com.hrms.performancemanagement.repositories.EvaluateCycleRepository;
import com.unboundid.util.NotNull;
import com.unboundid.util.Nullable;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;


@Service
@Transactional
public class EmployeeManagementServiceImpl implements EmployeeManagementService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmergencyContactRepository emergencyContactRepository;
    private final EmployeeDamInfoRepository employeeDamInfoRepository;
    private final SkillEvaluationRepository skillEvaluationRepository;
    private final DamService damService;
    private final CareerSpecification careerSpecification;
    private final PositionDepartmentRepository positionDepartmentRepository;
    private final SkillRepository skillRepository;
    private final PositionLevelSkillRepository positionLevelSkillRepository;
    private final EvaluateCycleRepository evaluateCycleRepository;
    private EvaluateCycle latestEvaluateCycle;

    private ModelMapper modelMapper;

    static String PROFILE_IMAGE = "PROFILE_IMAGE";
    static String QUALIFICATION = "QUALIFICATION";

    @Bean
    public void setUpMapper() {
        this.modelMapper = new ModelMapper();
    }

    @Autowired
    public EmployeeManagementServiceImpl(EmployeeRepository employeeRepository,
                                         DepartmentRepository departmentRepository,
                                         EmergencyContactRepository emergencyContactRepository,
                                         EmployeeDamInfoRepository employeeDamInfoRepository,
                                         SkillEvaluationRepository skillEvaluationRepository,
                                         DamService damService,
                                         CareerSpecification careerSpecification,
                                         PositionDepartmentRepository positionDepartmentRepository,
                                         SkillRepository skillRepository,
                                         EvaluateCycleRepository evaluateCycleRepository,
                                         PositionLevelSkillRepository positionLevelSkillRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.emergencyContactRepository = emergencyContactRepository;
        this.employeeDamInfoRepository = employeeDamInfoRepository;
        this.skillEvaluationRepository = skillEvaluationRepository;
        this.damService = damService;
        this.careerSpecification = careerSpecification;
        this.positionDepartmentRepository = positionDepartmentRepository;
        this.skillRepository = skillRepository;
        this.positionLevelSkillRepository = positionLevelSkillRepository;
        this.evaluateCycleRepository = evaluateCycleRepository;
    }

    @PostConstruct
    private void initialize() {
        this.latestEvaluateCycle = getLatestEvalCycle();
    }

    private EvaluateCycle getLatestEvalCycle() {
        return evaluateCycleRepository.findFirstByOrderByStartDateDesc();
    }

    @Override
    public List<Employee> getAllEmployees() {
        //Find all employee have status not equal "Terminated"
        Specification<Employee> spec = (root, query, builder) -> builder.notEqual(root.get("status"), false);
        return employeeRepository.findAll(spec);
    }

    @Override
    public List<Employee> getAllEmployeesHaveDepartment() {
        //Find all employee have status not equal 0 and department not null
        Specification<Employee> spec = (root, query, builder) -> builder.and(
                builder.notEqual(root.get("status"), false),
                builder.isNotNull(root.get("department"))
        );
        return employeeRepository.findAll(spec);
    }


    @Override
    public List<Employee> getEmployeesInDepartment(Integer departmentId) {
        Specification<Employee> spec = (root, query, builder) -> builder.notEqual(root.get("status"), 0);
        Specification<Employee> hasDepartment = GlobalSpec.hasDepartmentId(departmentId);
        return employeeRepository.findAll(spec.and(hasDepartment));
    }

    @Override
    public Employee findEmployee(Integer id) {
        Specification<Employee> spec = GlobalSpec.hasId(id);
        return employeeRepository
                .findOne(spec)
                .orElseThrow(() -> new RuntimeException("EmployeeDocument not found with id: " + id));
    }

    @Override
    public EmployeeDTO getEmployeeDetail(Integer id) {
        Specification<Employee> spec = GlobalSpec.hasId(id);
        Employee employee = employeeRepository
                .findOne(spec)
                .orElseThrow(() -> new RuntimeException("EmployeeDocument not found with id: " + id));
        Specification<EmergencyContact> contactSpec = GlobalSpec.hasEmployeeId(id);
        List<EmergencyContact> emergencyContacts = emergencyContactRepository.findAll(contactSpec);

        //Get employeeDamInfo have employeeId = id and type = "Profile Picture" and has the latest uploadedAt
        String imageUrl = employeeDamInfoRepository
                .findAll(EmployeeDamInfoSpec.hasEmployeeAndType(id, "PROFILE_IMAGE"))
                .stream()
                .max(Comparator.comparing(EmployeeDamInfo::getUploadedAt)).map(EmployeeDamInfo::getUrl)
                .orElse(null);

        if (employee.getPosition() == null || employee.getJobLevel() == null)
            return new EmployeeDTO(employee, imageUrl, emergencyContacts, Collections.emptyList());

        //Get Skills
        Specification<PositionLevelSkill> specSP = GlobalSpec.hasPositionId(employee.getPosition().getId());
        Specification<PositionLevelSkill> specSL = GlobalSpec.hasJobLevelId(employee.getJobLevel().getId());

        List<Skill> skills = positionLevelSkillRepository.findAll(specSP.and(specSL))
                .stream()
                .map(PositionLevelSkill::getSkill)
                .toList();

        return new EmployeeDTO(employee, imageUrl, emergencyContacts, skills);

    }

    @Override
    public List<EmployeeDTO> getNewEmployees() {
        Sort sort = Sort.by(Sort.Order.desc("joinedDate"));

        List<Employee> employees = employeeRepository.findAll(sort);
        List<ProfileImageOnly> images = employeeDamInfoRepository.findByEmployeeIdsSetAndFileType(
                employees.stream()
                        .map(Employee::getId).toList(), PROFILE_IMAGE);
        List<EmergencyContact> eContacts = emergencyContactRepository.findAll();

        List<PositionLevelSkill> pols = positionLevelSkillRepository.findAll();

        return employees.stream()
                .map(employee -> {
                    String url = images.stream()
                            .filter(image -> image.getEmployeeId().equals(employee.getId()))
                            .findFirst()
                            .map(ProfileImageOnly::getUrl)
                            .orElse(null);

                    List<EmergencyContact> contacts = eContacts.stream()
                            .filter(contact -> contact.getEmployee().getId().equals(employee.getId()))
                            .toList();

                    List<Skill> skills = pols.stream()
                            .filter(pls -> pls.getPosition().equals(employee.getPosition())
                                    && pls.getJobLevel().equals(employee.getJobLevel()))
                            .map(PositionLevelSkill::getSkill)
                            .toList();
                    return new EmployeeDTO(employee, url, contacts, skills);
                })
                .toList();
    }

    @Override
    public EmployeePagingDTO filterEmployees(@Nullable List<Integer> departmentIds,
                                             @Nullable List<Integer> currentContracts,
                                             @Nullable Boolean status,
                                             @Nullable String name,
                                             Integer pageNo, Integer pageSize) {
        Specification<Employee> filterSpec = (root, query, criteriaBuilder) -> criteriaBuilder.and(
                departmentIds != null && !departmentIds.isEmpty()
                        ? root.get("department").get("id").in(departmentIds)
                        : criteriaBuilder.conjunction(),
                currentContracts != null && !currentContracts.isEmpty()
                        ? root.get("currentContract").in(currentContracts)
                        : criteriaBuilder.conjunction(),
                status != null
                        ? criteriaBuilder.equal(root.get("status"), status)
                        : criteriaBuilder.conjunction(),
                name != null
                        ? criteriaBuilder.or(
                        criteriaBuilder.like(root.get("lastName"), "%" + name + "%"),
                        criteriaBuilder.like(root.get("firstName"), "%" + name + "%"))
                        : criteriaBuilder.conjunction()
        );

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Page<Employee> empPage = employeeRepository.findAll(filterSpec, pageable);

        List<ProfileImageOnly> images = getProfileImageOnlies(empPage);

        List<EmergencyContact> eContacts = emergencyContactRepository.findAll();

        List<PositionLevelSkill> pols = positionLevelSkillRepository.findAll();

        List<EmployeeDTO> listDTO = empPage.stream()
                .map(employee -> {
                    String url = images.stream()
                            .filter(image -> image.getEmployeeId().equals(employee.getId()))
                            .findFirst()
                            .map(ProfileImageOnly::getUrl)
                            .orElse(null);

                    List<EmergencyContact> contacts = eContacts.stream()
                            .filter(contact -> contact.getEmployee().getId().equals(employee.getId()))
                            .toList();

                    List<Skill> skills = pols.stream()
                            .filter(pls -> pls.getPosition().equals(employee.getPosition())
                                    && pls.getJobLevel().equals(employee.getJobLevel()))
                            .map(PositionLevelSkill::getSkill)
                            .toList();
                    return new EmployeeDTO(employee, url, contacts, skills);
                })
                .toList();

        Pagination pagination = PaginationSetup.setupPaging(empPage.getTotalElements(), pageNo, pageSize);
        return new EmployeePagingDTO(listDTO, pagination);
    }

    private List<ProfileImageOnly> getProfileImageOnlies(Page<Employee> empPage) {
        List<ProfileImageOnly> images = employeeDamInfoRepository.findByEmployeeIdsSetAndFileType(
                empPage.stream()
                        .map(Employee::getId).toList(), PROFILE_IMAGE);
        return images;
    }

    @Override
    public PercentageChangeDTO getHeadcountsStatistic() {
        //Get all new employees have joinedDate between 2 years ago and 1 year ago
        LocalDate datePrevious = LocalDate.now().minusYears(1);
        var countPreviousYearEmployees = countEmployeesByYear(datePrevious);

        //Get all new employees have joinedDate between today and 1 year ago
        LocalDate dateCurrent = LocalDate.now();
        var countCurrentYearEmployees = countEmployeesByYear(dateCurrent);

        Integer countAllEmployee = getAllEmployees().size();

        float diffPercent = ((float) (countCurrentYearEmployees - countPreviousYearEmployees) / countPreviousYearEmployees) * 100;

        return new PercentageChangeDTO(countAllEmployee, diffPercent, countPreviousYearEmployees <= countCurrentYearEmployees);
    }

    @Override
    public BarChartDTO getHeadcountChartData() {
        List<Department> department = departmentRepository.findAll();
        List<Integer> departmentIds = department.stream().map(Department::getId).toList();
        //Find all employees in departmentIds and have status not equal to 0
        Specification<Employee> spec = (root, query, builder) -> builder.notEqual(root.get("status"), 0);
        Specification<Employee> hasDepartmentIds = GlobalSpec.hasDepartmentIds(departmentIds);
        List<Employee> employees = employeeRepository.findAll(spec.and(hasDepartmentIds));

        List<DataItemDTO> items = department.stream().map(item -> {
            Float countEmployee = (float) employees
                    .stream()
                    .filter(employee -> employee.getDepartment().getId() == item.getId())
                    .count();
            return new DataItemDTO(item.getDepartmentName(), countEmployee);
        }).toList();

        return new BarChartDTO("Department's Employees", items);
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
        employee.setInsertionTime(new Date(System.currentTimeMillis()));
        employee.setModificationTime(new Date(System.currentTimeMillis()));
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
        if (employeeInputDTO.getEmergencyContacts() != null)
            manageEmergencyContacts(employeeInputDTO.getEmergencyContacts(), employee);

        return employee;
    }

    private void manageEmergencyContacts(List<EmergencyContactInputDTO> emergencyContacts, Employee employee) {
        deleteEmerContactNotInNewList(emergencyContacts, employee.getId());
        insertOrUpdateEmerContact(emergencyContacts, employee);
    }

    private void deleteEmerContactNotInNewList(List<EmergencyContactInputDTO> emergencyContacts, Integer employeeId) {
        // Get all emergency contacts of the employee
        Specification<EmergencyContact> spec = GlobalSpec.hasEmployeeId(employeeId);
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
        Specification<EmergencyContact> spec = GlobalSpec.hasIds(ecIds);
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
    public void uploadPersonalFile(MultipartFile file, Integer employeeId, String type) throws IOException {
        // Upload the image using the DamService with the original file name
        Map uploadResult = damService.uploadFile(file);
        // Get the file's extension like jpg, png, docx, ...
        String url = uploadResult.get("url").toString();
        // Update the employee's profile picture public ID in the database
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new RuntimeException("EmployeeDocument not found with id: " + employeeId));
        EmployeeDamInfo employeeDamInfo = EmployeeDamInfo.builder()
                .employee(employee)
                .fileName(file.getOriginalFilename())
                .type(type)
                .url(url)
                .uploadedAt(new Date(System.currentTimeMillis()))
                .build();
        employeeDamInfoRepository.save(employeeDamInfo);
    }

    @Override
    public String getProfilePicture(Integer employeeId) {
        Specification<EmployeeDamInfo> spec = (root, query, builder) -> builder.and(
                builder.equal(root.get("employee").get("id"), employeeId),
                builder.equal(root.get("type"), PROFILE_IMAGE)
        );
        EmployeeDamInfo employeeDamInfo = employeeDamInfoRepository.findOne(spec).orElse(null);
        return employeeDamInfo != null ? employeeDamInfo.getUrl() : null;
    }

    @Override
    public List<String> getProfilePictures(List<Integer> employeeIds) {
        Specification<EmployeeDamInfo> spec = (root, query, builder) -> builder.and(
                builder.in(root.get("employee").get("id")).value(employeeIds),
                builder.equal(root.get("type"), PROFILE_IMAGE)
        );
        List<EmployeeDamInfo> employeeDamInfos = employeeDamInfoRepository.findAll(spec);
        return employeeDamInfos.stream().map(EmployeeDamInfo::getUrl).toList();
    }

    @Override
    public List<EmployeeDamInfoDTO> getQualifications(Integer employeeId) {
        var spec = EmployeeDamInfoSpec.hasEmployeeAndType(employeeId, QUALIFICATION);
        return employeeDamInfoRepository.findAll(spec)
                .stream().map(e -> new EmployeeDamInfoDTO(e.getEmployee().getId(),
                        e.getExtension().getName(),
                        e.getUrl(),
                        e.getExtension().getIconUri(),
                        e.getUploadedAt()))
                .toList();
    }

    @Override
    public EmployeeOverviewDTO getProfileOverview(Integer employeeId) {
        skillRepository.findAll();

        Employee employee = employeeRepository.findOne(GlobalSpec.hasId(employeeId)).orElseThrow(() ->
                new RuntimeException("Employee not found with id: " + employeeId));

        Specification<SkillEvaluation> hasEmp = GlobalSpec.hasEmployeeId(employeeId);
        Specification<SkillEvaluation> hasCycle = GlobalSpec.hasEvaluateCycleId(latestEvaluateCycle.getId());

        List<String> skillSets = skillEvaluationRepository
                .findAll(hasEmp.and(hasCycle))
                .stream()
                .map(SkillEvaluation::getSkill)
                .toList()
                .stream().map(Skill::getSkillName).toList();

        String profileImgUri = getProfilePicture(employeeId);

        return new EmployeeOverviewDTO(employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                profileImgUri,
                employee.getPosition().getPositionName(),
                employee.getJobLevel().getJobLevelName(),
                skillSets,
                null);
    }

    @Override
    public List<SimpleItemDTO> getDepartmentEmployees(Integer departmentId, Integer positionId) {
        Specification<Employee> hasDepartment = careerSpecification.hasDepartmentId(departmentId);
        Specification<Employee> hasPosition = careerSpecification.hasPositionId(positionId);
        return employeeRepository.findAll(hasDepartment.and(hasPosition))
                .stream()
                .map(e -> new SimpleItemDTO(e.getId(), e.getFirstName() + " " + e.getLastName()))
                .toList();
    }

    public List<ProfileImageOnly> getEmployeesNameAndAvatar(List<Integer> idsSet) {
        return employeeDamInfoRepository.findByEmployeeIdsSetAndFileType(idsSet, PROFILE_IMAGE);
    }

    @Override
    public List<NameImageDTO> getNameImagesInDepartment(Integer departmentId) {
        List<Employee> departmentEmployees = getEmployeesInDepartment(departmentId);
        List<Integer> employeeIds = departmentEmployees.stream().map(Employee::getId).toList();
        List<ProfileImageOnly> urls = employeeDamInfoRepository.findByEmployeeIdsSetAndFileType(employeeIds, PROFILE_IMAGE);
        return departmentEmployees.stream().map(e -> {
            ProfileImageOnly profile = urls
                    .stream()
                    .filter(u -> u.getEmployeeId().equals(e.getId()))
                    .findFirst()
                    .orElse(null);
            String url = profile != null ? profile.getUrl() : null;
            return new NameImageDTO(e.getId(), e.getFirstName(), e.getLastName(), url);
        }).toList();
    }

    @Override
    public PercentageChangeDTO getDepartmentHeadcount(Integer departmentId) {
        //Get all new employees have joinedDate between 2 years ago and 1 year ago
        LocalDate datePrevious = LocalDate.now().minusYears(1);
        var countPreviousYearEmployees = countDepartmentEmployeesByYear(datePrevious, departmentId);

        //Get all new employees have joinedDate between today and 1 year ago
        LocalDate dateCurrent = LocalDate.now();
        var countCurrentYearEmployees = countDepartmentEmployeesByYear(dateCurrent, departmentId);

        Integer countAllEmployee = getEmployeesInDepartment(departmentId).size();

        float diffPercent = countPreviousYearEmployees != 0
                ? ((float) (countCurrentYearEmployees - countPreviousYearEmployees) / countPreviousYearEmployees) * 100
                : 0;

        return new PercentageChangeDTO(countAllEmployee, diffPercent, countPreviousYearEmployees <= countCurrentYearEmployees);
    }

    private long countDepartmentEmployeesByYear(LocalDate date, Integer departmentId) {
        //Get all new employees have joinedDate before date and have status not equal to 0
        Specification<Employee> spec = (root, query, builder) -> builder.and(
                builder.equal(root.get("department").get("id"), departmentId),
                builder.lessThan(root.get("joinedDate"), date),
                builder.notEqual(root.get("status"), 0)
        );

        return employeeRepository.count(spec);
    }

    @Override
    public BarChartDTO getDepartmentHeadcountChart(Integer departmentId) {
        Specification<DepartmentPosition> hasDepartment = GlobalSpec.hasDepartmentId(departmentId);
        List<DepartmentPosition> posDepartments = positionDepartmentRepository.findAll(hasDepartment);
        List<Position> positions = posDepartments.stream().map(DepartmentPosition::getPosition).toList();
        List<Integer> posIds = positions.stream().map(Position::getId).toList();
        //Find all employees in departmentIds and have status not equal to 0
        Specification<Employee> spec = (root, query, builder) -> builder.notEqual(root.get("status"), 0);
        Specification<Employee> eHasPositions = GlobalSpec.hasPositionIds(posIds);
        Specification<Employee> eHasDepartment = GlobalSpec.hasDepartmentId(departmentId);
        List<Employee> employees = employeeRepository.findAll(spec.and(eHasPositions).and(eHasDepartment));

        List<DataItemDTO> items = positions.stream().map(item -> {
            Float countEmployee = (float) employees
                    .stream()
                    .filter(employee -> employee.getPosition().getId() == item.getId())
                    .count();
            return new DataItemDTO(item.getPositionName(), countEmployee);
        }).toList();

        return new BarChartDTO("Position's Employees", items);
    }
}
