package com.hrms.performancemanagement.services.impl;

import com.hrms.careerpathmanagement.dto.DiffPercentDTO;
import com.hrms.careerpathmanagement.dto.EmployeePotentialPerformanceDTO;
import com.hrms.careerpathmanagement.dto.TimeLine;
import com.hrms.careerpathmanagement.models.CompetencyTimeLine;
import com.hrms.global.mapper.HrmsMapper;
import com.hrms.performancemanagement.input.PerformanceRangeInput;
import com.hrms.performancemanagement.model.PerformanceRange;
import com.hrms.careerpathmanagement.models.ProficiencyLevel;
import com.hrms.careerpathmanagement.input.EvaluationProcessInput;
import com.hrms.careerpathmanagement.models.Template;
import com.hrms.careerpathmanagement.repositories.PerformanceEvaluationRepository;
import com.hrms.careerpathmanagement.repositories.PerformanceRangeRepository;
import com.hrms.careerpathmanagement.repositories.ProficiencyLevelRepository;
import com.hrms.careerpathmanagement.repositories.TemplateRepository;
import com.hrms.employeemanagement.dto.EmployeeRatingDTO;
import com.hrms.employeemanagement.dto.pagination.EmployeeRatingPagination;
import com.hrms.employeemanagement.models.Department;
import com.hrms.employeemanagement.models.Employee;
import com.hrms.employeemanagement.models.JobLevel;
import com.hrms.employeemanagement.projection.EmployeeIdOnly;
import com.hrms.employeemanagement.repositories.DepartmentRepository;
import com.hrms.employeemanagement.repositories.EmployeeRepository;
import com.hrms.employeemanagement.repositories.JobLevelRepository;
import com.hrms.employeemanagement.services.EmployeeManagementService;
import com.hrms.employeemanagement.specification.EmployeeSpecification;
import com.hrms.global.GlobalSpec;
import com.hrms.global.dto.*;
import com.hrms.global.paging.Pagination;
import com.hrms.global.paging.PaginationSetup;
import com.hrms.performancemanagement.dto.DatasetDTO;
import com.hrms.performancemanagement.dto.StackedBarChart;
import com.hrms.performancemanagement.input.PerformanceCycleInput;
import com.hrms.performancemanagement.input.ProficiencyLevelInput;
import com.hrms.performancemanagement.model.PerformanceCycle;
import com.hrms.performancemanagement.model.PerformanceEvaluation;
import com.hrms.performancemanagement.model.PerformanceTimeLine;
import com.hrms.performancemanagement.repositories.PerformanceCycleRepository;
import com.hrms.performancemanagement.repositories.PerformanceTimeLineRepository;
import com.hrms.performancemanagement.services.PerformanceService;
import com.hrms.performancemanagement.specification.PerformanceSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class PerformanceServiceImpl implements PerformanceService {
    @PersistenceContext
    private EntityManager em;
    static String SELF_EVAL_LABEL_NAME = "Self Evaluation";
    static String SUPERVISOR_EVAL_LABEL_NAME = "Supervisor Evaluation";
    static String COMPLETED_LABEL_NAME = "Completed";
    static String IN_COMPLETED_LABEL_NAME = "InCompleted";
    private final EmployeeManagementService employeeService;
    private final PerformanceEvaluationRepository performanceEvaluationRepository;
    private final PerformanceCycleRepository performanceCycleRepository;
    private final JobLevelRepository jobLevelRepository;
    private final PerformanceRangeRepository performanceRangeRepository;
    private final EmployeeSpecification employeeSpecification;
    private final PerformanceSpecification performanceSpecification;
    private final DepartmentRepository departmentRepository;
    private final EmployeeManagementService employeeManagementService;
    private final PerformanceTimeLineRepository performanceTimeLineRepository;
    private final TemplateRepository templateRepository;
    private final EmployeeRepository employeeRepository;
    private final ProficiencyLevelRepository proficiencyLevelRepository;
    private HrmsMapper mapper;

    @Autowired
    public PerformanceServiceImpl(EmployeeManagementService employeeService,
                                  PerformanceEvaluationRepository performanceEvaluationRepository,
                                  PerformanceCycleRepository performanceCycleRepository,
                                  JobLevelRepository jobLevelRepository,
                                  PerformanceRangeRepository performanceRangeRepository,
                                  EmployeeSpecification employeeSpecification,
                                  PerformanceSpecification performanceSpecification,
                                  DepartmentRepository departmentRepository,
                                  EmployeeManagementService employeeManagementService,
                                  PerformanceTimeLineRepository performanceTimeLineRepository,
                                  EmployeeRepository employeeRepository,
                                  TemplateRepository templateRepository,
                                  ProficiencyLevelRepository proficiencyLevelRepository,
                                  HrmsMapper mapper) {
        this.employeeService = employeeService;
        this.performanceEvaluationRepository = performanceEvaluationRepository;
        this.performanceCycleRepository = performanceCycleRepository;
        this.jobLevelRepository = jobLevelRepository;
        this.performanceRangeRepository = performanceRangeRepository;
        this.employeeSpecification = employeeSpecification;
        this.performanceSpecification = performanceSpecification;
        this.departmentRepository = departmentRepository;
        this.employeeManagementService = employeeManagementService;
        this.performanceTimeLineRepository = performanceTimeLineRepository;
        this.employeeRepository = employeeRepository;
        this.templateRepository = templateRepository;
        this.proficiencyLevelRepository = proficiencyLevelRepository;
        this.mapper = mapper;
    }

    @Override
    public List<PerformanceCycle> getAllPerformanceCycles() {
        return performanceCycleRepository.findAll();
    }

    public Float getAveragePerformanceScore(Integer cycleId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Double> query = cb.createQuery(Double.class);
        Root<PerformanceEvaluation> root = query.from(PerformanceEvaluation.class);

        query.select(cb.avg(root.get("finalAssessment")))
                .where(cb.equal(root.get("performanceCycle").get("performanceCycleId"), cycleId));

        return em.createQuery(query).getSingleResult().floatValue();
    }

    @Override
    public Page<PerformanceEvaluation> getPerformanceEvaluations(Integer empId, Pageable pageable) {
        Specification<PerformanceEvaluation> spec = employeeSpecification.hasEmployeeId(empId);
        return performanceEvaluationRepository.findAll(spec, pageable);
    }


    public List<PerformanceEvaluation> getEvaluations(Integer positionId, Integer performanceCycleId) {
        Specification<PerformanceEvaluation> positionFilter = employeeSpecification.hasPositionId(positionId);
        Specification<PerformanceEvaluation> cycleFilter = performanceSpecification.hasCycleId(performanceCycleId);

        return performanceEvaluationRepository.findAll(positionFilter.and(cycleFilter));
    }

    /**
     * Performance Ranges : Unsatisfactory, Needs Improvement, Meets Expectations, Exceeds Expectations, Outstanding...
     * A score belong to a range if it is greater than or equal to the min value and less than or equal to the max value
     * Ex: Meets Expectation range has min value = 3 and max value = 4
     * Label is a column in the chart. In this case, it is job level
     *
     * @param positionId
     * @param cycleId
     * @return
     */
    @Override
    public StackedBarChart getPerformanceByJobLevel(Integer positionId, Integer cycleId) {
        var evaluations = performanceEvaluationRepository.findByCycleIdAndPositionId(positionId, cycleId);

        var performanceRanges = performanceRangeRepository.findAll();

        var labels = jobLevelRepository.findAll();

        var datasets = createDatasets(evaluations, performanceRanges, labels);

        DatasetDTO notEvaluated = new DatasetDTO("Not Evaluated", new ArrayList<>());
        labels.forEach(label -> {
            Float percentage = getPercentNotEvaluated(label, cycleId);
            notEvaluated.addData(percentage);
        });

        datasets.add(notEvaluated);

        return new StackedBarChart(labels, datasets);
    }

    public BarChartDTO getPerformanceRatingScheme(Integer cycleId, Integer departmentId) {
        Specification<PerformanceEvaluation> cycleSpec = performanceSpecification.hasCycleId(cycleId);
        Specification<PerformanceEvaluation> depSpec = departmentId == null ? null : employeeSpecification.hasDepartmentId(departmentId);

        var evaluations = performanceEvaluationRepository.findAll(cycleSpec.and(depSpec));

        var ratingScheme = performanceRangeRepository.findAll();

        var data = new ArrayList();

        ratingScheme.forEach(item -> {
            var count = evaluations.stream().filter(e -> e.getFinalAssessment() >= item.getMinValue())
                    .filter(e -> e.getFinalAssessment() <= item.getMaxValue())
                    .count();
            data.add(new DataItemDTO(item.getText(), (float) count));
        });

        return new BarChartDTO("Performance Rating Scheme", data);
    }

    @Override
    public DiffPercentDTO getPerformanceOverview(Integer cycleId, Integer departmentId) {
        var averageScore = averagePerformanceScore(cycleId, departmentId);
        var averageScoreLastCycle = averagePerformanceScore(cycleId - 1, departmentId);
        var diffPercent = (averageScoreLastCycle == 0) ? 0 : (averageScore - averageScoreLastCycle) / averageScoreLastCycle * 100;
        return new DiffPercentDTO(averageScore, 5.0f, diffPercent, averageScore > averageScoreLastCycle);
    }

    private Float averagePerformanceScore(Integer cycleId, Integer departmentId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Float> query = cb.createQuery(Float.class);
        Root<PerformanceEvaluation> root = query.from(PerformanceEvaluation.class);

        Predicate cyclePredicate = cycleId == null ? null : cb.equal(root.get("performanceCycle").get("id"), cycleId);
        Predicate departmentPredicate = departmentId == null ? null : cb.equal(root.get("employee").get("department").get("id"), departmentId);
        Predicate[] predicates = Arrays.stream(new Predicate[]{cyclePredicate, departmentPredicate})
                .filter(p -> p != null)
                .toArray(Predicate[]::new);

        query.select(cb.avg(root.get("finalAssessment")).as(Float.class))
                .where(predicates);
        return em.createQuery(query).getSingleResult();
    }

    @Override
    public List<EmployeePotentialPerformanceDTO> getPotentialAndPerformance(Integer departmentId, Integer cycleId) {
        Specification<PerformanceEvaluation> empSpec = employeeSpecification.hasDepartmentId(departmentId);
        Specification<PerformanceEvaluation> cycleSpec = performanceSpecification.hasCycleId(cycleId);

        var evaluations = performanceEvaluationRepository.findAll(empSpec.and(cycleSpec));

        var results = new ArrayList<EmployeePotentialPerformanceDTO>();

        evaluations.forEach(item -> results.add(new EmployeePotentialPerformanceDTO(
                item.getEmployee().getId(),
                item.getEmployee().getFullName(),
                employeeService.getProfilePicture(item.getEmployee().getId()),
                item.getPotentialScore(),
                item.getFinalAssessment())
        ));
        return results;
    }

    @Override
    public List<EmployeePotentialPerformanceDTO> getPotentialAndPerformanceByPosition(Integer departmentId,
                                                                                      Integer cycleId,
                                                                                      Integer positionId)
    {
        var result = getPotentialAndPerformance(departmentId, cycleId);

        var empIdsSetHasPosition = new HashSet<>(employeeRepository.findAllByDepartmentId(departmentId, EmployeeIdOnly.class)
            .stream().map(EmployeeIdOnly::id).toList());

        return result.stream().filter(i -> empIdsSetHasPosition.contains(i.getEmployeeId())).toList();
    }

    @Override
    public EmployeeRatingPagination getPerformanceRating(Integer departmentId, Integer cycleId, PageRequest pageable) {
        List<Integer> departmentEmployeeIds = (departmentId != null) ?
                employeeManagementService.getEmployeesInDepartment(departmentId)
                        .stream()
                        .map(Employee::getId)
                        .toList() :
                Collections.emptyList();

        Specification<PerformanceEvaluation> hasEmployeeIds = GlobalSpec.hasEmployeeIds(departmentEmployeeIds);
        Specification<PerformanceEvaluation> cycleSpec = GlobalSpec.hasPerformCycleId(cycleId);
        Specification<PerformanceEvaluation> spec = (departmentId != null) ?
                hasEmployeeIds.and(cycleSpec) :
                cycleSpec;

        Page<PerformanceEvaluation> evaluations = performanceEvaluationRepository.findAll(spec, pageable);

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
                                            List<JobLevel> labels) {
        List<DatasetDTO> datasets = new ArrayList<>();

        performanceRanges.forEach(range -> {
            var dataset = createDatasetForRange(evaluations, range, labels);
            datasets.add(dataset);
        });

        return datasets;
    }

    private DatasetDTO createDatasetForRange(List<PerformanceEvaluation> evaluations,
                                             PerformanceRange range,
                                             List<JobLevel> labels) {
        DatasetDTO datasetDTO = new DatasetDTO(range.getText(), new ArrayList<>());
        labels.forEach(label -> {
            long total = countEvals(label, evaluations);
            long count = countRatingSchemeByJoblevel(label, range, evaluations);
            Float percentage = calculatePercentage(count, total);
            datasetDTO.addData(percentage);
        });
        return datasetDTO;
    }

    private long countEvals(JobLevel jobLevel, List<PerformanceEvaluation> evaluations) {
        return evaluations.stream()
                .filter(e -> e.getEmployee().getJobLevel().getId() == jobLevel.getId())
                .count();
    }

    private long countRatingSchemeByJoblevel(JobLevel jobLevel, PerformanceRange range, List<PerformanceEvaluation> evaluations) {
        return evaluations.stream()
                .filter(eval -> eval.getEmployee().getJobLevel().getId() == jobLevel.getId())
                .filter(eval -> eval.getFinalAssessment() >= range.getMinValue())
                .filter(eval -> eval.getFinalAssessment() <= range.getMaxValue())
                .count();
    }

    private Float getPercentNotEvaluated(JobLevel jobLevel, Integer cycleId) {
        Specification<Employee> hasJobLevel = EmployeeSpecification.hasJobLevel(jobLevel.getId());
        var countEmp = employeeRepository.count(hasJobLevel);

        Specification<PerformanceEvaluation> hasCycle = performanceSpecification.hasPerformanceCycleId(cycleId);
        Specification<PerformanceEvaluation> hasJobLevelEval = employeeSpecification.hasJobLevelId(jobLevel.getId());
        var countEval = performanceEvaluationRepository.count(hasCycle.and(hasJobLevelEval));

        return countEval == 0 ? 0 : (float) (countEmp - countEval) / countEmp * 100;
    }

    private Float calculatePercentage(long amount, long total) {
        return total == 0 ? 0 : (float) (amount * 100) / total;
    }


    @Override
    public DataItemPagingDTO getEmployeePerformanceRatingScore(Integer employeeId,
                                                               Integer pageNo, Integer pageSize) {
        performanceCycleRepository.findAll();

        Specification<PerformanceEvaluation> spec = GlobalSpec.hasEmployeeId(employeeId);
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

    @Override
    public MultiBarChartDTO getDepartmentInCompletePerform(Integer cycleId) {
        List<Department> departments = departmentRepository.findAll();
        //Get all employees have department not null
        List<Employee> employees = employeeManagementService.getAllEmployeesHaveDepartment();

        List<Float> selfData = processDepartmentData(departments, SELF_EVAL_LABEL_NAME, cycleId, employees);
        List<Float> managerData = processDepartmentData(departments, SUPERVISOR_EVAL_LABEL_NAME, cycleId, employees);

        List<MultiBarChartDataDTO> datasets = new ArrayList<>();
        datasets.add(new MultiBarChartDataDTO(SELF_EVAL_LABEL_NAME, selfData));
        datasets.add(new MultiBarChartDataDTO(SUPERVISOR_EVAL_LABEL_NAME, managerData));

        List<String> labels = departments.stream().map(Department::getDepartmentName).toList();
        return new MultiBarChartDTO(labels, datasets);
    }

    private List<Float> processDepartmentData(List<Department> departments, String type,
                                              Integer cycleId, List<Employee> employees) {
        return departments.parallelStream().map(item -> {
            List<Integer> departmentEmpIds = employees.stream()
                    .filter(emp -> emp.getDepartment().getId() == item.getId())
                    .map(Employee::getId)
                    .toList();

            return (type.equals(SELF_EVAL_LABEL_NAME))
                    ? getEmployeeInCompletedPercent(cycleId, departmentEmpIds)
                    : getEvaluatorInCompletePercent(cycleId, departmentEmpIds);
        }).toList();
    }

    private Float getEmployeeInCompletedPercent(Integer cycleId, List<Integer> empIdSet) {
        return calculatePercentage(cycleId, empIdSet, "selfAssessment");
    }

    private Float getEvaluatorInCompletePercent(Integer cycleId, List<Integer> empIdSet) {
        return calculatePercentage(cycleId, empIdSet, "supervisorAssessment");
    }

    private Float calculatePercentage(Integer cycleId, List<Integer> empIdSet, String assessmentType) {
        if (empIdSet.isEmpty()) return null;
        //Get all performance evaluations
        //have employeeId in empIdSet and roleField is not null and performanceCycleId = cycleId
        Specification<PerformanceEvaluation> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get(assessmentType));

        Specification<PerformanceEvaluation> hasEmployees = GlobalSpec.hasEmployeeIds(empIdSet);
        Specification<PerformanceEvaluation> hasCycle = GlobalSpec.hasPerformCycleId(cycleId);

        var completedCount = performanceEvaluationRepository.count(spec.and(hasEmployees).and(hasCycle));
        return (float) (empIdSet.size() - completedCount) / empIdSet.size() * 100;
    }

    @Override
    public PieChartDTO getPerformanceEvalProgress(Integer performanceCycleId) {
        List<Integer> empIdSet = employeeManagementService.getAllEmployees()
                .stream()
                .map(Employee::getId)
                .toList();

        //get all employees who have completed evaluation
        Specification<PerformanceEvaluation> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), "Completed");

        Specification<PerformanceEvaluation> hasEmployees = GlobalSpec.hasEmployeeIds(empIdSet);
        Specification<PerformanceEvaluation> hasCycle = GlobalSpec.hasPerformCycleId(performanceCycleId);

        List<Float> datasets = new ArrayList<>();
        var completedPercent = (float) performanceEvaluationRepository
                .count(spec.and(hasEmployees).and(hasCycle)) / empIdSet.size() * 100;
        datasets.add(completedPercent);
        datasets.add(100 - completedPercent);

        return new PieChartDTO(List.of(COMPLETED_LABEL_NAME, IN_COMPLETED_LABEL_NAME), datasets);
    }

    @Override
    public List<TimeLine> getPerformanceTimeLine(Integer cycleId) {
        Specification<PerformanceTimeLine> spec = GlobalSpec.hasPerformCycleId(cycleId);
        return performanceTimeLineRepository.findAll(spec)
                .stream()
                .map(item -> new TimeLine(
                        item.getPerformanceTimeLineName(),
                        item.getStartDate().toString(), item.getDueDate().toString(),
                        item.getIsDone()))
                .toList();
    }

    @Override
    public PerformanceCycle createPerformanceCycle(PerformanceCycleInput input) {
        PerformanceCycle cycle = mapper.map(input, PerformanceCycle.class);
        cycle.setInsertionTime(new Date());
        cycle.setModificationTime(new Date());
        Template template = templateRepository.findAll(GlobalSpec.hasId(input.getTemplate()))
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Template not found"));
        cycle.setTemplate(template);

        performanceCycleRepository.save(cycle);

        return cycle;
    }

    @Override
    public ProficiencyLevel updateProficiencyLevel(Integer id, ProficiencyLevelInput input) {
        var proficiencyLevel = proficiencyLevelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proficiency level not found"));
        proficiencyLevel.setProficiencyLevelName(input.getName());
        proficiencyLevel.setProficiencyLevelDescription(input.getDescription());
        proficiencyLevel.setScore(input.getScore());
        return proficiencyLevelRepository.save(proficiencyLevel);
    }

    @Override
    public PerformanceRange updatePerformanceRange(Integer id, PerformanceRangeInput input) {
        var performanceRange = performanceRangeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Performance range not found"));
        performanceRange.setMinValue(input.getMinValue());
        performanceRange.setMaxValue(input.getMaxValue());
        performanceRange.setText(input.getText());
        return performanceRangeRepository.save(performanceRange);
    }
    public String performanceCyclePeriod(Integer cycleId) {
        PerformanceCycle cycle = performanceCycleRepository.findAll(GlobalSpec.hasId(cycleId))
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cycle not found"));
        return String.format("%s - %s", cycle.getPerformanceCycleStartDate(), cycle.getPerformanceCycleEndDate());
    }

    @Override
    public List<TimeLine> createPerformanceProcess(EvaluationProcessInput input) throws ParseException {
        List<PerformanceTimeLine> performTimeLines = input.getTimeLines()
                .stream()
                .map(tl -> mapper.map(tl, PerformanceTimeLine.class))
                .toList();

        PerformanceCycle cycle = performanceCycleRepository.findAll(GlobalSpec.hasId(input.getCycleId()))
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cycle not found"));

        performTimeLines.forEach(tl -> tl.setPerformanceCycle(cycle));

        performanceTimeLineRepository.saveAll(performTimeLines);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        cycle.setInitialDate(dateFormat.parse(input.getInitialDate()));
        performanceCycleRepository.save(cycle);

        return mapper.map(performTimeLines, new TypeToken<List<TimeLine>>() {}.getType());
    }

}