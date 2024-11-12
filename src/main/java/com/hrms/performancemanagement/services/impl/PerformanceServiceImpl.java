package com.hrms.performancemanagement.services.impl;

import com.hrms.careerpathmanagement.dto.*;
import com.hrms.careerpathmanagement.repositories.CategoryRepository;
import com.hrms.careerpathmanagement.repositories.QuestionRepository;
import com.hrms.employeemanagement.dto.EmployeeRatingDTO;
import com.hrms.employeemanagement.dto.pagination.EmployeeRatingPagination;
import com.hrms.employeemanagement.models.Employee;
import com.hrms.employeemanagement.projection.EmployeeIdOnly;
import com.hrms.employeemanagement.repositories.DepartmentRepository;
import com.hrms.employeemanagement.repositories.EmployeeRepository;
import com.hrms.employeemanagement.repositories.JobLevelRepository;
import com.hrms.employeemanagement.services.EmployeeManagementService;
import com.hrms.employeemanagement.specification.EmployeeSpecification;
import com.hrms.global.GlobalSpec;
import com.hrms.global.dto.*;
import com.hrms.global.models.*;
import com.hrms.global.paging.Pagination;
import com.hrms.global.paging.PaginationSetup;
import com.hrms.performancemanagement.dto.*;
import com.hrms.performancemanagement.model.PerformanceEvaluation;
import com.hrms.performancemanagement.model.PerformanceEvaluationOverall;
import com.hrms.performancemanagement.model.PerformanceRange;
import com.hrms.performancemanagement.projection.IdOnly;
import com.hrms.performancemanagement.repositories.EvaluateCycleRepository;
import com.hrms.performancemanagement.repositories.PerformanceEvaluationOverallRepository;
import com.hrms.performancemanagement.repositories.PerformanceEvaluationRepository;
import com.hrms.performancemanagement.repositories.PerformanceRangeRepository;
import com.hrms.performancemanagement.services.PerformanceService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
    private final PerformanceEvaluationOverallRepository performanceEvaluationOverallRepository;
    private final EvaluateCycleRepository evaluateCycleRepository;
    private final JobLevelRepository jobLevelRepository;
    private final PerformanceRangeRepository performanceRangeRepository;
    private final EmployeeSpecification employeeSpecification;
    private final DepartmentRepository departmentRepository;
    private final CategoryRepository categoryRepository;
    private final QuestionRepository questionRepository;
    private final PerformanceEvaluationRepository performanceEvaluationRepository;
    private final EmployeeManagementService employeeManagementService;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public PerformanceServiceImpl(EmployeeManagementService employeeService,
                                  PerformanceEvaluationOverallRepository performanceEvaluationOverallRepository,
                                  EvaluateCycleRepository evaluateCycleRepository,
                                  JobLevelRepository jobLevelRepository,
                                  PerformanceRangeRepository performanceRangeRepository,
                                  EmployeeSpecification employeeSpecification,
                                  CategoryRepository categoryRepository,
                                  QuestionRepository questionRepository,
                                  PerformanceEvaluationRepository performanceEvaluationRepository,
                                  DepartmentRepository departmentRepository,
                                  EmployeeManagementService employeeManagementService,
                                  EmployeeRepository employeeRepository) {
        this.employeeService = employeeService;
        this.performanceEvaluationOverallRepository = performanceEvaluationOverallRepository;
        this.evaluateCycleRepository = evaluateCycleRepository;
        this.jobLevelRepository = jobLevelRepository;
        this.performanceRangeRepository = performanceRangeRepository;
        this.employeeSpecification = employeeSpecification;
        this.categoryRepository = categoryRepository;
        this.questionRepository = questionRepository;
        this.performanceEvaluationRepository = performanceEvaluationRepository;
        this.departmentRepository = departmentRepository;
        this.employeeManagementService = employeeManagementService;
        this.employeeRepository = employeeRepository;
    }


    public Float getAveragePerformanceScore(Integer cycleId) {
        var evalIds = performanceEvaluationOverallRepository.findAllByCycleId(cycleId, IdOnly.class)
                .stream()
                .map(IdOnly::id)
                .toList();
        return performanceEvaluationOverallRepository.avgEvalScoreByIdIn(evalIds).floatValue();
    }

    @Override
    public Page<PerformanceEvaluationOverall> getPerformanceEvaluations(Integer empId, Pageable pageable) {
        Specification<PerformanceEvaluationOverall> spec = employeeSpecification.hasEmployeeId(empId);
        return performanceEvaluationOverallRepository.findAll(spec, pageable);
    }


    @Override
    public StackedBarChart getPerformanceByJobLevel(Integer positionId, Integer cycleId) {
        Specification<PerformanceEvaluationOverall> hasCycle = GlobalSpec.hasEvaluateCycleId(cycleId);
        Specification<PerformanceEvaluationOverall> hasPosition = GlobalSpec.hasPositionId(positionId);

        List<PerformanceEvaluationOverall> evaluations = (positionId == null || positionId == -1)
                ? performanceEvaluationOverallRepository.findAll(hasCycle)
                : performanceEvaluationOverallRepository.findAll(hasCycle.and(hasPosition));

        var performanceRanges = performanceRangeRepository.findAll();

        var labels = jobLevelRepository.findAll();

        var datasets = createDatasets(evaluations, performanceRanges, positionId, labels, cycleId);

        return new StackedBarChart(labels, datasets);
    }

    public BarChartDTO performanceOverviewChart(Integer cycleId, Integer departmentId) {
        Specification<PerformanceEvaluationOverall> cycleSpec = GlobalSpec.hasEvaluateCycleId(cycleId);
        Specification<PerformanceEvaluationOverall> depSpec = departmentId == null ? null : employeeSpecification.hasDepartmentId(departmentId);

        var evaluations = performanceEvaluationOverallRepository.findAll(cycleSpec.and(depSpec));

        var ratingScheme = performanceRangeRepository.findAll();

        var data = ratingScheme.stream().map(item -> {
            var count = evaluations.stream().filter(e -> e.getFinalAssessment() >= item.getMinValue())
                    .filter(e -> e.getFinalAssessment() <= item.getMaxValue())
                    .count();
            return new DataItemDTO(item.getText(), (float) count);
        }).toList();

        return new BarChartDTO("Performance Rating Scheme", data);
    }

    @Override
    public DiffPercentDTO performanceDiffPercent(Integer cycleId, Integer departmentId) {
        var maxScore = 5.0f;

        var averageScore = averagePerformanceScore(cycleId, departmentId);
        EvaluateCycle currentCycle = evaluateCycleRepository.findById(cycleId).orElseThrow();
        EvaluateCycle previousCycle = evaluateCycleRepository.findByYear(currentCycle.getYear() - 1);

        if (previousCycle == null)
            return new DiffPercentDTO(averageScore, maxScore, (float) 100, true);

        var averageScoreLastCycle = averagePerformanceScore(previousCycle.getId(), departmentId);
        var diffPercent = (averageScore - averageScoreLastCycle) / averageScoreLastCycle * 100;
        return new DiffPercentDTO(averageScore, maxScore, diffPercent, diffPercent > 0);
    }

    private Float averagePerformanceScore(Integer cycleId, Integer departmentId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Float> query = cb.createQuery(Float.class);
        Root<PerformanceEvaluationOverall> root = query.from(PerformanceEvaluationOverall.class);

        Predicate cyclePredicate = cb.equal(root.get("evaluateCycle").get("id"), cycleId);
        Predicate departmentPredicate = departmentId == null ? null : cb.equal(root.get("employee").get("department").get("id"), departmentId);
        Predicate[] predicates = Arrays.stream(new Predicate[]{cyclePredicate, departmentPredicate})
                .filter(Objects::nonNull)
                .toArray(Predicate[]::new);

        query.select(cb.avg(root.get("finalAssessment")).as(Float.class))
                .where(predicates);
        return em.createQuery(query).getSingleResult();
    }

    @Override
    public List<EmployeePotentialPerformanceDTO> getPotentialAndPerformance(Integer departmentId, Integer cycleId) {
        Specification<PerformanceEvaluationOverall> empSpec = employeeSpecification.hasDepartmentId(departmentId);
        Specification<PerformanceEvaluationOverall> cycleSpec = GlobalSpec.hasEvaluateCycleId(cycleId);

        var evaluations = (departmentId == null || departmentId == -1)
                ? performanceEvaluationOverallRepository.findAll(cycleSpec)
                : performanceEvaluationOverallRepository.findAll(empSpec.and(cycleSpec));

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
                                                                                      Integer cycleId) {
        var result = getPotentialAndPerformance(departmentId, cycleId);

        var empIdsSetHasPosition = new HashSet<>(employeeRepository.findAllByDepartmentId(departmentId, EmployeeIdOnly.class)
                .stream().map(EmployeeIdOnly::id).toList());

        return result.stream()
                .filter(i -> empIdsSetHasPosition.contains(i.getEmployeeId()))
                .toList();
    }

    @Override
    public EmployeeRatingPagination getPerformanceRating(Integer departmentId, Integer cycleId, PageRequest pageable) {
        List<Integer> departmentEmployeeIds = (departmentId != null) ?
                employeeManagementService.getEmployeesInDepartment(departmentId)
                        .stream()
                        .map(Employee::getId)
                        .toList() :
                Collections.emptyList();

        Specification<PerformanceEvaluationOverall> hasEmployeeIds = GlobalSpec.hasEmployeeIds(departmentEmployeeIds);
        Specification<PerformanceEvaluationOverall> cycleSpec = GlobalSpec.hasEvaluateCycleId(cycleId);
        Specification<PerformanceEvaluationOverall> spec = (departmentId != null) ?
                hasEmployeeIds.and(cycleSpec) :
                cycleSpec;

        Page<PerformanceEvaluationOverall> evaluations = performanceEvaluationOverallRepository.findAll(spec, pageable);

        List<EmployeeRatingDTO> results = new ArrayList<>();
        evaluations.forEach(item ->
                results.add(new EmployeeRatingDTO(
                        item.getEmployee().getId(),
                        item.getEmployee().getFirstName(),
                        item.getEmployee().getLastName(),
                        employeeService.getProfilePicture(item.getEmployee().getId()),
                        item.getFinalAssessment()
                )));

        Pagination pagination = PaginationSetup.setupPaging(evaluations.getTotalElements(), pageable.getPageNumber(), pageable.getPageSize());
        return new EmployeeRatingPagination(results, pagination);
    }

    private List<DatasetDTO> createDatasets(List<PerformanceEvaluationOverall> evaluations,
                                            List<PerformanceRange> performanceRanges,
                                            Integer positionId,
                                            List<JobLevel> labels, Integer cycleId) {
        List<DatasetDTO> datasets = new ArrayList<>();

        DatasetDTO notEvaluated = new DatasetDTO("Not Evaluated", new ArrayList<>());
        labels.forEach(label -> {
            Float percentage = getPercentNotEvaluated(label, positionId, cycleId);
            notEvaluated.addData(percentage);
        });

        datasets.add(notEvaluated);

        performanceRanges.forEach(range -> {
            var dataset = createDatasetForRange(evaluations, range, labels, positionId);
            datasets.add(dataset);
        });

        return datasets;
    }

    private DatasetDTO createDatasetForRange(List<PerformanceEvaluationOverall> evaluations,
                                             PerformanceRange range,
                                             List<JobLevel> labels, Integer positionId) {
        DatasetDTO datasetDTO = new DatasetDTO(range.getText(), new ArrayList<>());
        labels.forEach(label -> {
            long total = countEmployees(label, positionId);
            long count = countRatingSchemeByJobLevel(label, range, evaluations, positionId);
            Float percentage = calculatePercentage(count, total);
            datasetDTO.addData(percentage);
        });
        return datasetDTO;
    }

    private long countEmployees(JobLevel jobLevel, Integer positionId) {
        Specification<Employee> hasJobLevel = GlobalSpec.hasJobLevelId(jobLevel.getId());
        Specification<Employee> hasPosition = GlobalSpec.hasPositionId(positionId);
        Specification<Employee> hasStatus = GlobalSpec.hasStatusTrue();

        return (positionId == null || positionId == -1)
                ? employeeRepository.count(hasJobLevel.and(hasStatus))
                : employeeRepository.count(hasJobLevel.and(hasStatus).and(hasPosition));
    }

    private long countRatingSchemeByJobLevel(JobLevel jobLevel, PerformanceRange range,
                                             List<PerformanceEvaluationOverall> evaluations, Integer positionId) {
        return (positionId == null || positionId == -1)
                ? evaluations.stream()
                .filter(eval -> Objects.equals(eval.getEmployee().getJobLevel().getId(), jobLevel.getId()))
                .filter(eval -> eval.getFinalAssessment() >= range.getMinValue())
                .filter(eval -> eval.getFinalAssessment() <= range.getMaxValue())
                .count()
                : evaluations.stream()
                .filter(eval -> Objects.equals(eval.getEmployee().getPosition().getId(), positionId))
                .filter(eval -> Objects.equals(eval.getEmployee().getJobLevel().getId(), jobLevel.getId()))
                .filter(eval -> eval.getFinalAssessment() >= range.getMinValue())
                .filter(eval -> eval.getFinalAssessment() <= range.getMaxValue())
                .count();
    }

    private Float getPercentNotEvaluated(JobLevel jobLevel, Integer positionId, Integer cycleId) {
        Specification<Employee> hasJobLevel = EmployeeSpecification.hasJobLevel(jobLevel.getId());
        Specification<Employee> hasPosition = GlobalSpec.hasPositionId(positionId);
        var countEmp = (positionId == null || positionId == -1)
                ? employeeRepository.count(hasJobLevel)
                : employeeRepository.count(hasJobLevel.and(hasPosition));

        Specification<PerformanceEvaluationOverall> hasCycle = GlobalSpec.hasEvaluateCycleId(cycleId);
        Specification<PerformanceEvaluationOverall> hasJobLevelEval = employeeSpecification.hasJobLevelId(jobLevel.getId());
        Specification<PerformanceEvaluationOverall> hasPositionEval = employeeSpecification.hasPositionId(positionId);
        var countEval = (positionId == null || positionId == -1)
                ? performanceEvaluationOverallRepository.count(hasCycle.and(hasJobLevelEval))
                : performanceEvaluationOverallRepository.count(hasCycle.and(hasJobLevelEval).and(hasPositionEval));

        return countEval == 0 ? 100 : (float) (countEmp - countEval) / countEmp * 100;
    }

    private Float calculatePercentage(long amount, long total) {
        return total == 0 ? 0 : (float) (amount * 100) / total;
    }


    @Override
    public DataItemPagingDTO getEmployeePerformanceRatingScore(Integer employeeId,
                                                               Integer pageNo, Integer pageSize) {

        Specification<PerformanceEvaluationOverall> spec = GlobalSpec.hasEmployeeId(employeeId);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<PerformanceEvaluationOverall> page = performanceEvaluationOverallRepository.findAll(spec, pageable);
        List<DataItemDTO> data = page.map(item -> new DataItemDTO(
                item.getEvaluateCycle().getEvaluateCycleName(),
                item.getFinalAssessment()
        )).getContent();

        Pagination pagination = PaginationSetup.setupPaging(page.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize());

        return new DataItemPagingDTO(data, pagination);
    }

    @Override
    public PieChartDTO getPerformanceEvalProgress(Integer cycleId, Integer departmentId) {
        List<Integer> empIdSet = employeeManagementService.getEmployeesInDepartment(departmentId)
                .stream()
                .map(Employee::getId)
                .toList();

        //get all employees who have completed evaluation
        Specification<PerformanceEvaluationOverall> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("finalStatus"), "Completed");

        Specification<PerformanceEvaluationOverall> hasEmployees = GlobalSpec.hasEmployeeIds(empIdSet);
        Specification<PerformanceEvaluationOverall> hasCycle = GlobalSpec.hasEvaluateCycleId(cycleId);

        List<Float> datasets = new ArrayList<>();
        var completedPercent = (float) performanceEvaluationOverallRepository
                .count(spec.and(hasEmployees).and(hasCycle)) / empIdSet.size() * 100;
        datasets.add(completedPercent);
        datasets.add(100 - completedPercent);

        return new PieChartDTO(List.of(COMPLETED_LABEL_NAME, IN_COMPLETED_LABEL_NAME), datasets);
    }

    public String performanceCyclePeriod(Integer cycleId) {
        EvaluateCycle cycle = evaluateCycleRepository.findAll(GlobalSpec.hasId(cycleId))
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cycle not found"));
        return String.format("%s - %s", cycle.getStartDate(), cycle.getDueDate());
    }

    @Override
    public PieChartDTO getPerformancePieChartOverall(EvaluateCycle cycle) {
        if (cycle.getStatus().equals("Not Start")) return null;
        List<PerformanceRange> ranges = performanceRangeRepository.findAll();
        Specification<PerformanceEvaluationOverall> hasCycleId = GlobalSpec.hasEvaluateCycleId(cycle.getId());
        List<PerformanceEvaluationOverall> pes = performanceEvaluationOverallRepository.findAll(hasCycleId);

        List<String> labels = new ArrayList<>(ranges.stream().map(PerformanceRange::getText).toList());
        List<Float> datasets = new ArrayList<>(ranges.stream()
                .map(r -> {
                    long count = pes.stream()
                            .filter(pe ->
                                    pe.getFinalAssessment() != null &&
                                            pe.getFinalAssessment() >= r.getMinValue() &&
                                            pe.getFinalAssessment() <= r.getMaxValue()
                            )
                            .count();

                    return calculatePercent((int) count, pes.size());
                })
                .toList());

        long notEval = pes.stream().filter(pe -> pe.getFinalAssessment() == null).count();
        float percentNotEval = calculatePercent((int) notEval, pes.size());

        labels.add(0, "Not evaluate yet");
        datasets.add(0, percentNotEval);

        return new PieChartDTO(labels, datasets);
    }

    private float calculatePercent(int number, int total) {
        return ((float) number / total) * 100;
    }

    @Override
    public MultiBarChartDTO getCompletedEvaluationByPosition(Integer cycleId, Integer departmentId) {
        List<Department> departments = departmentRepository.findAllByIsEvaluate(true);
        List<Employee> employees = employeeManagementService.getAllEmployeesHaveDepartment();

        //Get all CompetencyEvaluationOverall of all employees have final status is agreed and get the latest cycle
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
                    .filter(emp -> Objects.equals(emp.getDepartment().getId(), item.getId()))
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
        return calculatePercentage(cycleId, empIdSet, "evaluatorAssessment");
    }

    private Float calculatePercentage(Integer cycleId, List<Integer> empIdSet, String assessmentType) {
        if (empIdSet.isEmpty()) return null;
        //Get all performance evaluations
        //have employeeId in empIdSet and roleField is not null and performanceCycleId = cycleId
        Specification<PerformanceEvaluationOverall> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get(assessmentType));

        Specification<PerformanceEvaluationOverall> hasEmployees = GlobalSpec.hasEmployeeIds(empIdSet);
        Specification<PerformanceEvaluationOverall> hasCycle = GlobalSpec.hasEvaluateCycleId(cycleId);

        var completedCount = performanceEvaluationOverallRepository.count(spec.and(hasEmployees).and(hasCycle));
        return (float) (empIdSet.size() - completedCount) / empIdSet.size() * 100;
    }

    @Override
    public PerformanceOverall getPerformanceOverall(Integer employeeId, Integer cycleId) {
        Specification<PerformanceEvaluationOverall> hasEmployeeId = GlobalSpec.hasEmployeeId(employeeId);
        Specification<PerformanceEvaluationOverall> hasCycleId = GlobalSpec.hasEvaluateCycleId(cycleId);
        PerformanceEvaluationOverall pEOverall = performanceEvaluationOverallRepository
                .findOne(hasEmployeeId.and(hasCycleId))
                .orElseThrow();

        EvaluateCycle cycle = evaluateCycleRepository.findById(cycleId).orElseThrow();
        Employee employee = employeeRepository.findById(employeeId).orElseThrow();
        String profileImage = employeeManagementService.getProfilePicture(employeeId);
        String rating = (pEOverall.getSelfAssessment() != null)
                ? getPerformanceRank(pEOverall.getSelfAssessment())
                : "Not evaluate yet";

        return PerformanceOverall.builder()
                .evaluationCycleName(cycle.getEvaluateCycleName())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .profileImage(profileImage)
                .position(employee.getPosition().getPositionName())
                .level(employee.getJobLevel().getJobLevelName())
                .isSubmit(pEOverall.getEmployeeStatus().equals("Completed"))
                .rating(rating)
                .status((pEOverall.getEmployeeStatus() != null)
                        ? pEOverall.getEmployeeStatus()
                        : "Not Start")
                .build();
    }

    private String getPerformanceRank(Float score) {
        List<PerformanceRange> performanceRanges = performanceRangeRepository.findAll();
        return performanceRanges.stream()
                .filter(pR -> score >= pR.getMinValue() && score <= pR.getMaxValue())
                .findFirst()
                .map(PerformanceRange::getText)
                .orElseThrow();
    }

    @Override
    public List<PerformanceCategoryRating> getPerformanceCategoryRating(Integer employeeId, Integer cycleId) {
        Specification<PerformanceEvaluation> hasEmployeeId = GlobalSpec.hasEmployeeId(employeeId);
        Specification<PerformanceEvaluation> hasCycleId = GlobalSpec.hasEvaluateCycleId(cycleId);
        List<PerformanceEvaluation> cPs = performanceEvaluationRepository.findAll(hasEmployeeId.and(hasCycleId));

        List<Category> cs = categoryRepository.findAll();

        return cs.stream().map(cg -> {
            Float rating = !cPs.isEmpty() ? (float) cPs.stream()
                    .filter(eval -> eval.getQuestion().getCategory().getId().equals(cg.getId()))
                    .mapToDouble(cE -> cE.getSelfEvaluation() != null ? cE.getSelfEvaluation() : 0)
                    .average()
                    .orElse(0) : 0;
            return new PerformanceCategoryRating(cg.getId(), cg.getCategoryName(), cg.getCategoryDescription(),
                    cg.getCategoryWeight(), rating);
        }).toList();
    }

    @Override
    public List<PerformanceQuestionRating> getPerformanceQuestionRating(Integer employeeId, Integer cycleId) {
        List<Question> qs = questionRepository.findAll();

        Specification<PerformanceEvaluation> hasEmployeeId = GlobalSpec.hasEmployeeId(employeeId);
        Specification<PerformanceEvaluation> hasCycleId = GlobalSpec.hasEvaluateCycleId(cycleId);
        List<PerformanceEvaluation> pEs = performanceEvaluationRepository.findAll(hasEmployeeId.and(hasCycleId));

        return qs.stream()
                .map(q -> {
                    Float competencyRating = !pEs.isEmpty()
                            && getPerformanceEvaluation(pEs, q.getId()).getSelfEvaluation() != null
                            ? getPerformanceEvaluation(pEs, q.getId()).getSelfEvaluation()
                            : 1;
                    String comment = !pEs.isEmpty()
                            && getPerformanceEvaluation(pEs, q.getId()).getSelfComment() != null
                            ? getPerformanceEvaluation(pEs, q.getId()).getSelfComment()
                            : "";

                    return new PerformanceQuestionRating(q.getId(), q.getQuestionName(), q.getQuestionDescription(),
                            comment, competencyRating, q.getCategory().getId());
                }).toList();
    }

    private PerformanceEvaluation getPerformanceEvaluation(List<PerformanceEvaluation> pEs, Integer competencyId) {
        return pEs.stream()
                .filter(eval -> eval.getQuestion().getId().equals(competencyId))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public PerformanceOverall getManagerPerformanceOverall(Integer employeeId, Integer cycleId) {
        Specification<PerformanceEvaluationOverall> hasEmployeeId = GlobalSpec.hasEmployeeId(employeeId);
        Specification<PerformanceEvaluationOverall> hasCycleId = GlobalSpec.hasEvaluateCycleId(cycleId);
        PerformanceEvaluationOverall pEOverall = performanceEvaluationOverallRepository
                .findOne(hasEmployeeId.and(hasCycleId))
                .orElseThrow();

        EvaluateCycle cycle = evaluateCycleRepository.findById(cycleId).orElseThrow();
        Employee employee = employeeRepository.findById(employeeId).orElseThrow();
        String profileImage = employeeManagementService.getProfilePicture(employeeId);
        String rating = (pEOverall.getEvaluatorAssessment() != null)
                ? getPerformanceRank(pEOverall.getEvaluatorAssessment())
                : "Not evaluate yet";

        return PerformanceOverall.builder()
                .evaluationCycleName(cycle.getEvaluateCycleName())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .profileImage(profileImage)
                .position(employee.getPosition().getPositionName())
                .level(employee.getJobLevel().getJobLevelName())
                .isSubmit(pEOverall.getEvaluatorStatus().equals("Completed"))
                .rating(rating)
                .status((pEOverall.getEvaluatorStatus() != null)
                        ? pEOverall.getEvaluatorStatus()
                        : "Not Start")
                .build();
    }

    @Override
    public List<PerformanceCategoryRating> getManagerPerformanceCategoryRating(Integer employeeId, Integer cycleId) {
        Specification<PerformanceEvaluation> hasEmployeeId = GlobalSpec.hasEmployeeId(employeeId);
        Specification<PerformanceEvaluation> hasCycleId = GlobalSpec.hasEvaluateCycleId(cycleId);
        List<PerformanceEvaluation> cPs = performanceEvaluationRepository.findAll(hasEmployeeId.and(hasCycleId));

        List<Category> cs = categoryRepository.findAll();

        return cs.stream().map(cg -> {
            Float rating = !cPs.isEmpty() ? (float) cPs.stream()
                    .filter(eval -> eval.getQuestion().getCategory().getId().equals(cg.getId()))
                    .mapToDouble(cE -> cE.getSupervisorEvaluation() != null ? cE.getSupervisorEvaluation() : 0)
                    .average()
                    .orElse(0) : 0;
            return new PerformanceCategoryRating(cg.getId(), cg.getCategoryName(), cg.getCategoryDescription(),
                    cg.getCategoryWeight(), rating);
        }).toList();
    }

    @Override
    public List<PerformanceQuestionRating> getManagerPerformanceQuestionRating(Integer employeeId, Integer cycleId) {
        List<Question> qs = questionRepository.findAll();

        Specification<PerformanceEvaluation> hasEmployeeId = GlobalSpec.hasEmployeeId(employeeId);
        Specification<PerformanceEvaluation> hasCycleId = GlobalSpec.hasEvaluateCycleId(cycleId);
        List<PerformanceEvaluation> pEs = performanceEvaluationRepository.findAll(hasEmployeeId.and(hasCycleId));

        return qs.stream()
                .map(q -> {
                    Float competencyRating = !pEs.isEmpty()
                            && getPerformanceEvaluation(pEs, q.getId()).getSupervisorEvaluation() != null
                            ? getPerformanceEvaluation(pEs, q.getId()).getSupervisorEvaluation()
                            : 1;
                    String comment = !pEs.isEmpty()
                            && getPerformanceEvaluation(pEs, q.getId()).getSupervisorComment() != null
                            ? getPerformanceEvaluation(pEs, q.getId()).getSupervisorComment()
                            : "";

                    return new PerformanceQuestionRating(q.getId(), q.getQuestionName(), q.getQuestionDescription(),
                            comment, competencyRating, q.getCategory().getId());
                }).toList();
    }

    @Override
    public PerformanceOverall getFinalPerformanceOverall(Integer employeeId, Integer cycleId) {
        Specification<PerformanceEvaluationOverall> hasEmployeeId = GlobalSpec.hasEmployeeId(employeeId);
        Specification<PerformanceEvaluationOverall> hasCycleId = GlobalSpec.hasEvaluateCycleId(cycleId);
        PerformanceEvaluationOverall pEOverall = performanceEvaluationOverallRepository
                .findOne(hasEmployeeId.and(hasCycleId))
                .orElseThrow();

        EvaluateCycle cycle = evaluateCycleRepository.findById(cycleId).orElseThrow();
        Employee employee = employeeRepository.findById(employeeId).orElseThrow();
        String profileImage = employeeManagementService.getProfilePicture(employeeId);
        String rating = (pEOverall.getFinalAssessment() != null)
                ? getPerformanceRank(pEOverall.getFinalAssessment())
                : "Not evaluate yet";

        return PerformanceOverall.builder()
                .evaluationCycleName(cycle.getEvaluateCycleName())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .profileImage(profileImage)
                .position(employee.getPosition().getPositionName())
                .level(employee.getJobLevel().getJobLevelName())
                .isSubmit(pEOverall.getFinalStatus().equals("Completed"))
                .rating(rating)
                .status((pEOverall.getFinalStatus() != null)
                        ? pEOverall.getFinalStatus()
                        : "Not Start")
                .build();
    }

    @Override
    public List<PerformanceCategoryRating> getFinalPerformanceCategoryRating(Integer employeeId, Integer cycleId) {
        Specification<PerformanceEvaluation> hasEmployeeId = GlobalSpec.hasEmployeeId(employeeId);
        Specification<PerformanceEvaluation> hasCycleId = GlobalSpec.hasEvaluateCycleId(cycleId);
        List<PerformanceEvaluation> cPs = performanceEvaluationRepository.findAll(hasEmployeeId.and(hasCycleId));

        List<Category> cs = categoryRepository.findAll();

        return cs.stream().map(cg -> {
            Float rating = !cPs.isEmpty() ? (float) cPs.stream()
                    .filter(eval -> eval.getQuestion().getCategory().getId().equals(cg.getId()))
                    .mapToDouble(cE -> cE.getFinalEvaluation() != null ? cE.getFinalEvaluation() : 0)
                    .average()
                    .orElse(0) : 0;
            return new PerformanceCategoryRating(cg.getId(), cg.getCategoryName(), cg.getCategoryDescription(),
                    cg.getCategoryWeight(), rating);
        }).toList();
    }

    @Override
    public List<PerformanceQuestionRating> getFinalPerformanceQuestionRating(Integer employeeId, Integer cycleId) {
        List<Question> qs = questionRepository.findAll();

        Specification<PerformanceEvaluation> hasEmployeeId = GlobalSpec.hasEmployeeId(employeeId);
        Specification<PerformanceEvaluation> hasCycleId = GlobalSpec.hasEvaluateCycleId(cycleId);
        List<PerformanceEvaluation> pEs = performanceEvaluationRepository.findAll(hasEmployeeId.and(hasCycleId));

        return qs.stream()
                .map(q -> {
                    Float competencyRating = !pEs.isEmpty()
                            && getPerformanceEvaluation(pEs, q.getId()).getFinalEvaluation() != null
                            ? getPerformanceEvaluation(pEs, q.getId()).getFinalEvaluation()
                            : 1;
                    String comment = !pEs.isEmpty()
                            && getPerformanceEvaluation(pEs, q.getId()).getFinalComment() != null
                            ? getPerformanceEvaluation(pEs, q.getId()).getFinalComment()
                            : "";

                    return new PerformanceQuestionRating(q.getId(), q.getQuestionName(), q.getQuestionDescription(),
                            comment, competencyRating, q.getCategory().getId());
                }).toList();
    }

    @Override
    public EvaluationPaging getCompetencyEvaluationList(Integer departmentId, Integer cycleId, String name, Integer pageNo, Integer pageSize) {
        Page<Employee> es = filterListDepartmentEmployee(name, departmentId, pageNo, pageSize);
        List<PerformanceEvaluationOverall> cEOs = getDepartmentEvaOverall(cycleId, es);


        List<EmployeeEvaProgress> data = es.stream().map(e -> {
            String profileImage = employeeManagementService.getProfilePicture(e.getId());
            PerformanceEvaluationOverall cEO = cEOs.stream()
                    .filter(item -> item.getEmployee().getId().equals(e.getId()))
                    .findFirst()
                    .orElseThrow();

            return EmployeeEvaProgress.builder()
                    .employeeId(e.getId())
                    .profileImage(profileImage)
                    .firstName(e.getFirstName())
                    .lastName(e.getLastName())
                    .position(e.getPosition().getPositionName())
                    .level(e.getJobLevel().getJobLevelName())
                    .employeeStatus(cEO.getEmployeeStatus())
                    .evaluatorStatus(cEO.getEvaluatorStatus())
                    .finalStatus(cEO.getFinalStatus())
                    .build();
        }).toList();

        Pagination pagination = PaginationSetup.setupPaging(es.getTotalElements(), pageNo, pageSize);
        return new EvaluationPaging(data, pagination);
    }

    private List<PerformanceEvaluationOverall> getDepartmentEvaOverall(Integer cycleId, Page<Employee> es) {
        Specification<PerformanceEvaluationOverall> hasCycle = GlobalSpec.hasEvaluateCycleId(cycleId);
        Specification<PerformanceEvaluationOverall> hasEmployees = GlobalSpec
                .hasEmployeeIds(es.stream().map(Employee::getId).toList());

        return performanceEvaluationOverallRepository.findAll(hasCycle.and(hasEmployees));
    }

    private Page<Employee> filterListDepartmentEmployee(String name, Integer departmentId, Integer pageNo, Integer pageSize) {
        Specification<Employee> filterSpec = (root, query, criteriaBuilder) -> criteriaBuilder.and(
                name != null
                        ? criteriaBuilder.or(
                        criteriaBuilder.like(root.get("lastName"), "%" + name + "%"),
                        criteriaBuilder.like(root.get("firstName"), "%" + name + "%"))
                        : criteriaBuilder.conjunction()
        );

        Specification<Employee> spec = (root, query, builder) -> builder.notEqual(root.get("status"), false);
        Specification<Employee> hasEval = (root, query, builder) -> builder.equal(root.get("isEvaluate"), true);
        Specification<Employee> hasDepartment = GlobalSpec.hasDepartmentId(departmentId);

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        return employeeRepository.findAll(filterSpec.and(spec.and(hasEval).and(hasDepartment)), pageable);
    }

    @Override
    public Boolean createEmployeeEvaluation(PerformanceEvaluationInput input) {
        List<PerformanceEvaluation> pEs = updateEmployeePerformanceEvaluations(input);
        updateEmployeePerformanceCompetencyEvaluationOverall(input, pEs);
        return Boolean.TRUE;
    }

    private List<PerformanceEvaluation> updateEmployeePerformanceEvaluations(PerformanceEvaluationInput input) {
        Specification<PerformanceEvaluation> hasEmployee = GlobalSpec.hasEmployeeId(input.getEmployeeId());
        Specification<PerformanceEvaluation> hasCycle = GlobalSpec.hasEvaluateCycleId(input.getCycleId());
        List<PerformanceEvaluation> pEs = performanceEvaluationRepository.findAll(hasEmployee.and(hasCycle));

        List<PerformanceEvaluation> updatedPerformanceEvaluation = input.getQuestionRating().stream()
                .flatMap(qR -> pEs.stream()
                        .filter(pE -> pE.getQuestion().getId().equals(qR.getQuestionId()))
                        .peek(pE -> {
                            pE.setSelfEvaluation(qR.getRating().floatValue());
                            pE.setSelfComment(qR.getComment());
                        })
                ).toList();

        return performanceEvaluationRepository.saveAll(updatedPerformanceEvaluation);
    }

    private void updateEmployeePerformanceCompetencyEvaluationOverall(PerformanceEvaluationInput input, List<PerformanceEvaluation> pEs) {
        Specification<PerformanceEvaluationOverall> hasEmployee = GlobalSpec.hasEmployeeId(input.getEmployeeId());
        Specification<PerformanceEvaluationOverall> hasCycle = GlobalSpec.hasEvaluateCycleId(input.getCycleId());
        PerformanceEvaluationOverall pEO = performanceEvaluationOverallRepository.findOne(hasEmployee.and(hasCycle))
                .orElseThrow();

        pEO.setSelfAssessment(getEmployeeOverallScore(pEs));
        pEO.setEmployeeStatus(input.getIsSubmit() ? "Completed" : "In Progress");
        pEO.setLastUpdated(new Date(System.currentTimeMillis()));
        performanceEvaluationOverallRepository.save(pEO);
    }

    private Float getEmployeeOverallScore(List<PerformanceEvaluation> pEs) {
        List<Category> categories = categoryRepository.findAll();

        List<Float> categoryRating = categories.stream().map(c -> (float) (pEs.stream()
                .filter(pE -> pE.getQuestion().getCategory().getId().equals(c.getId()))
                .mapToDouble(PerformanceEvaluation::getSelfEvaluation)
                .average()
                .orElse(0) * c.getCategoryWeight()) / 100).toList();

        return (float) categoryRating.stream().mapToDouble(Float::floatValue).sum();
    }

    @Override
    public Boolean createManagerEvaluation(PerformanceEvaluationInput input) {
        List<PerformanceEvaluation> pEs = updateManagerPerformanceEvaluations(input);
        updateManagerPerformanceCompetencyEvaluationOverall(input, pEs);
        return Boolean.TRUE;
    }

    private List<PerformanceEvaluation> updateManagerPerformanceEvaluations(PerformanceEvaluationInput input) {
        Specification<PerformanceEvaluation> hasEmployee = GlobalSpec.hasEmployeeId(input.getEmployeeId());
        Specification<PerformanceEvaluation> hasCycle = GlobalSpec.hasEvaluateCycleId(input.getCycleId());
        List<PerformanceEvaluation> pEs = performanceEvaluationRepository.findAll(hasEmployee.and(hasCycle));

        List<PerformanceEvaluation> updatedPerformanceEvaluation = input.getQuestionRating().stream()
                .flatMap(qR -> pEs.stream()
                        .filter(pE -> pE.getQuestion().getId().equals(qR.getQuestionId()))
                        .peek(pE -> {
                            pE.setSupervisorEvaluation(qR.getRating().floatValue());
                            pE.setSupervisorComment(qR.getComment());
                        })
                ).toList();

        return performanceEvaluationRepository.saveAll(updatedPerformanceEvaluation);
    }

    private void updateManagerPerformanceCompetencyEvaluationOverall(PerformanceEvaluationInput input, List<PerformanceEvaluation> pEs) {
        Specification<PerformanceEvaluationOverall> hasEmployee = GlobalSpec.hasEmployeeId(input.getEmployeeId());
        Specification<PerformanceEvaluationOverall> hasCycle = GlobalSpec.hasEvaluateCycleId(input.getCycleId());
        PerformanceEvaluationOverall pEO = performanceEvaluationOverallRepository.findOne(hasEmployee.and(hasCycle))
                .orElseThrow();

        pEO.setEvaluatorAssessment(getManagerOverallScore(pEs));
        pEO.setEvaluatorStatus(input.getIsSubmit() ? "Completed" : "In Progress");
        pEO.setLastUpdated(new Date(System.currentTimeMillis()));
        performanceEvaluationOverallRepository.save(pEO);
    }

    private Float getManagerOverallScore(List<PerformanceEvaluation> pEs) {
        List<Category> categories = categoryRepository.findAll();

        List<Float> categoryRating = categories.stream().map(c -> (float) (pEs.stream()
                .filter(pE -> pE.getQuestion().getCategory().getId().equals(c.getId()))
                .mapToDouble(PerformanceEvaluation::getSupervisorEvaluation)
                .average()
                .orElse(0) * c.getCategoryWeight()) / 100).toList();

        return (float) categoryRating.stream().mapToDouble(Float::floatValue).sum();
    }

    @Override
    public Boolean createFinalEvaluation(PerformanceEvaluationInput input) {
        List<PerformanceEvaluation> pEs = updateFinalPerformanceEvaluations(input);
        updateFinalPerformanceCompetencyEvaluationOverall(input, pEs);
        return Boolean.TRUE;
    }

    private List<PerformanceEvaluation> updateFinalPerformanceEvaluations(PerformanceEvaluationInput input) {
        Specification<PerformanceEvaluation> hasEmployee = GlobalSpec.hasEmployeeId(input.getEmployeeId());
        Specification<PerformanceEvaluation> hasCycle = GlobalSpec.hasEvaluateCycleId(input.getCycleId());
        List<PerformanceEvaluation> pEs = performanceEvaluationRepository.findAll(hasEmployee.and(hasCycle));

        List<PerformanceEvaluation> updatedPerformanceEvaluation = input.getQuestionRating().stream()
                .flatMap(qR -> pEs.stream()
                        .filter(pE -> pE.getQuestion().getId().equals(qR.getQuestionId()))
                        .peek(pE -> {
                            pE.setFinalEvaluation(qR.getRating().floatValue());
                            pE.setFinalComment(qR.getComment());
                        })
                ).toList();

        return performanceEvaluationRepository.saveAll(updatedPerformanceEvaluation);
    }

    private void updateFinalPerformanceCompetencyEvaluationOverall(PerformanceEvaluationInput input, List<PerformanceEvaluation> pEs) {
        Specification<PerformanceEvaluationOverall> hasEmployee = GlobalSpec.hasEmployeeId(input.getEmployeeId());
        Specification<PerformanceEvaluationOverall> hasCycle = GlobalSpec.hasEvaluateCycleId(input.getCycleId());
        PerformanceEvaluationOverall pEO = performanceEvaluationOverallRepository.findOne(hasEmployee.and(hasCycle))
                .orElseThrow();

        pEO.setFinalAssessment(getFinalOverallScore(pEs));
        pEO.setFinalStatus(input.getIsSubmit() ? "Completed" : "In Progress");
        pEO.setLastUpdated(new Date(System.currentTimeMillis()));
        if (input.getIsSubmit()) pEO.setCompletedDate(new Date(System.currentTimeMillis()));
        performanceEvaluationOverallRepository.save(pEO);
    }

    private Float getFinalOverallScore(List<PerformanceEvaluation> pEs) {
        List<Category> categories = categoryRepository.findAll();

        List<Float> categoryRating = categories.stream().map(c -> (float) (pEs.stream()
                .filter(pE -> pE.getQuestion().getCategory().getId().equals(c.getId()))
                .mapToDouble(PerformanceEvaluation::getFinalEvaluation)
                .average()
                .orElse(0) * c.getCategoryWeight()) / 100).toList();

        return (float) categoryRating.stream().mapToDouble(Float::floatValue).sum();
    }

    public void initEmployeesEvaluation(Integer cycleId) {
        List<Employee> employees = employeeManagementService.getAllEmployeesEvaluate();
        EvaluateCycle cycle = evaluateCycleRepository.findById(cycleId).orElseThrow();
        List<Question> questions = questionRepository.findAll();

        List<PerformanceEvaluationOverall> pEOs = newListEvaluationOverall(employees, cycle);
        List<PerformanceEvaluation> pEs = newListQuestionEvaluation(employees, questions, cycle);

        performanceEvaluationOverallRepository.saveAll(pEOs);
        performanceEvaluationRepository.saveAll(pEs);
    }

    private List<PerformanceEvaluationOverall> newListEvaluationOverall(List<Employee> employees, EvaluateCycle cycle) {
        return employees.stream()
                .map(e -> PerformanceEvaluationOverall.builder()
                        .employee(e)
                        .evaluateCycle(cycle)
                        .selfAssessment((float) 0)
                        .employeeStatus("In Progress")
                        .evaluatorAssessment((float) 0)
                        .evaluatorStatus("In Progress")
                        .finalAssessment((float) 0)
                        .finalStatus("In Progress")
                        .potentialScore((float) 0)
                        .lastUpdated(new Date(System.currentTimeMillis()))
                        .build())
                .toList();
    }

    private List<PerformanceEvaluation> newListQuestionEvaluation(List<Employee> employees,
                                                                  List<Question> questions,
                                                                  EvaluateCycle cycle) {
        return employees.stream()
                .flatMap(e -> questions.stream()
                        .map(c -> PerformanceEvaluation.builder()
                                .evaluateCycle(cycle)
                                .employee(e)
                                .question(c)
                                .selfEvaluation((float) 0)
                                .supervisorEvaluation((float) 0)
                                .finalEvaluation((float) 0)
                                .build()))
                .toList();
    }

}