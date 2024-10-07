package com.hrms.careerpathmanagement.services.impl;

import com.hrms.careerpathmanagement.dto.*;
import com.hrms.careerpathmanagement.dto.pagination.EmployeeEvaProgressPaging;
import com.hrms.careerpathmanagement.input.CompetencyEvaluationInput;
import com.hrms.careerpathmanagement.input.EvaluationProcessInput;
import com.hrms.careerpathmanagement.models.*;
import com.hrms.careerpathmanagement.repositories.*;
import com.hrms.careerpathmanagement.services.CompetencyService;
import com.hrms.employeemanagement.dto.EmployeeRatingDTO;
import com.hrms.employeemanagement.dto.EmployeeStatusDTO;
import com.hrms.employeemanagement.dto.SimpleItemDTO;
import com.hrms.employeemanagement.projection.NameAndStatusOnly;
import com.hrms.employeemanagement.dto.pagination.EmployeeRatingPagination;
import com.hrms.employeemanagement.models.*;
import com.hrms.employeemanagement.projection.ProfileImageOnly;
import com.hrms.employeemanagement.specification.EmployeeSpecification;
import com.hrms.global.GlobalSpec;
import com.hrms.global.dto.*;
import com.hrms.global.mapper.HrmsMapper;
import com.hrms.global.models.*;
import com.hrms.global.paging.Pagination;
import com.hrms.employeemanagement.repositories.*;
import com.hrms.employeemanagement.services.EmployeeManagementService;
import com.hrms.global.repositories.DepartmentPositionRepository;
import com.hrms.performancemanagement.model.PerformanceEvaluationOverall;
import com.hrms.performancemanagement.repositories.EvaluateCycleRepository;
import com.hrms.performancemanagement.repositories.EvaluateTimeLineRepository;
import com.hrms.performancemanagement.services.PerformanceService;
import com.mysema.commons.lang.Pair;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.hrms.global.paging.PaginationSetup.setupPaging;

@Service
@Transactional
@Slf4j
public class CompetencyServiceImpl implements CompetencyService {
    @PersistenceContext
    EntityManager entityManager;

    static final String SELF_EVAL_LABEL_NAME = "Self Evaluation";
    static final String SUPERVISOR_EVAL_LABEL_NAME = "Supervisor";
    static final String FINAL_EVAL_LABEL_NAME = "Final Score";
    static final String COMPLETED_LABEL_NAME = "Completed";
    static final String IN_COMPLETED_LABEL_NAME = "InCompleted";
    static final String COMPETENCY_COMPLETED_STATUS = "Completed";
    static final String PROFILE_IMAGE = "PROFILE_IMAGE";


    private final CompetencyEvaluationRepository competencyEvaluationRepository;
    private final EmployeeRepository employeeRepository;
    private final EvaluateTimeLineRepository evaluateTimeLineRepository;
    private final SkillEvaluationRepository skillEvaluationRepository;
    private final PositionLevelSkillRepository positionLevelSkillRepository;
    private final CompetencyRepository competencyRepository;
    private final ProficiencyLevelRepository proficiencyLevelRepository;
    private final SkillRepository skillRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeManagementService employeeManagementService;
    private final JobLevelRepository jobLevelRepository;
    private final EmployeeDamInfoRepository employeeDamInfoRepository;
    private final EmployeeSpecification employeeSpecification;
    private final EvaluateCycleRepository evaluateCycleRepository;
    private final DepartmentPositionRepository departmentPositionRepository;
    private final HrmsMapper modelMapper;
    private final CompetencyGroupRepository competencyGroupRepository;
    private final CompetencyEvaluationOverallRepository competencyEvaluationOverallRepository;
    private final PerformanceService performanceService;
    private EvaluateCycle latestEvaluateCycle;

    @Autowired
    public CompetencyServiceImpl(CompetencyEvaluationRepository competencyEvaluationRepository,
                                 EmployeeRepository employeeRepository,
                                 EvaluateTimeLineRepository evaluateTimeLineRepository,
                                 SkillEvaluationRepository skillEvaluationRepository,
                                 CompetencyRepository competencyRepository,
                                 ProficiencyLevelRepository proficiencyLevelRepository,
                                 SkillRepository skillRepository,
                                 DepartmentRepository departmentRepository,
                                 EmployeeManagementService employeeManagementService,
                                 JobLevelRepository jobLevelRepository,
                                 PositionLevelSkillRepository positionLevelSkillRepository,
                                 EmployeeDamInfoRepository employeeDamInfoRepository,
                                 DepartmentPositionRepository departmentPositionRepository,
                                 EmployeeSpecification employeeSpecification,
                                 EvaluateCycleRepository evaluateCycleRepository,
                                 HrmsMapper modelMapper,
                                 CompetencyGroupRepository competencyGroupRepository,
                                 CompetencyEvaluationOverallRepository competencyEvaluationOverallRepository,
                                 PerformanceService performanceService
    ) {
        this.competencyEvaluationRepository = competencyEvaluationRepository;
        this.employeeRepository = employeeRepository;
        this.evaluateTimeLineRepository = evaluateTimeLineRepository;
        this.skillEvaluationRepository = skillEvaluationRepository;
        this.positionLevelSkillRepository = positionLevelSkillRepository;
        this.competencyRepository = competencyRepository;
        this.proficiencyLevelRepository = proficiencyLevelRepository;
        this.skillRepository = skillRepository;
        this.departmentRepository = departmentRepository;
        this.departmentPositionRepository = departmentPositionRepository;
        this.employeeManagementService = employeeManagementService;
        this.jobLevelRepository = jobLevelRepository;
        this.employeeDamInfoRepository = employeeDamInfoRepository;
        this.employeeSpecification = employeeSpecification;
        this.evaluateCycleRepository = evaluateCycleRepository;
        this.modelMapper = modelMapper;
        this.competencyGroupRepository = competencyGroupRepository;
        this.competencyEvaluationOverallRepository = competencyEvaluationOverallRepository;
        this.performanceService = performanceService;
    }

    @PostConstruct
    private void initialize() {
        this.latestEvaluateCycle = getLatestEvaluateCycle();
        evaluateTimeLineRepository.updateIsDoneForOverdueItems();
    }


    @Override
    @Scheduled(cron = "0 0 0 * * *")
    public void updateIsDoneForOverdueItems() {
        evaluateTimeLineRepository.updateIsDoneForOverdueItems();
    }

    public EvaluateCycle getLatestEvaluateCycle() {
        return evaluateCycleRepository.findFirstByOrderByStartDateDesc();
    }

    /**
     * if skillEval is null, currentScore will be null
     * if targetSkill is null, targetScore will be null
     *
     * @return SkillSummarization (DTO)
     */

    private double getSkillTargetAvgScore(Employee employee, Integer cycleId) {
        List<SkillEvaluation> ssEvaluates = getSkillEvaluations(employee.getId(), cycleId);
        List<PositionLevelSkill> skillsTargets = getSkillsTarget(employee, ssEvaluates);

        return skillsTargets
                .stream()
                .mapToDouble(st -> st.getProficiencyLevel().getScore())
                .average()
                .orElse(0.0);
    }


    public BarChartDTO getSkillGap(Integer employeeId, Integer cycleId) {
        //1. Skill Average Score
        double skillAvgScore = getSelfSkillAvgScore(employeeId, cycleId);

        Employee employee = employeeRepository.findById(employeeId).orElseThrow();

        double skillTargetAvgScore = getSkillTargetAvgScore(employee, cycleId);

        DataItemDTO current = new DataItemDTO("Current", (float) skillAvgScore);
        DataItemDTO target = new DataItemDTO("Target", (float) skillTargetAvgScore);
        return new BarChartDTO("Skill Gap", List.of(current, target));
    }

    public PieChartDTO getCompetencyLevelPieChart(Integer employeeId, Integer cycleId) {
        Specification<CompetencyEvaluationOverall> hasCycleId = GlobalSpec.hasEvaluateCycleId(cycleId);
        Specification<CompetencyEvaluationOverall> hasEmployeeId = GlobalSpec.hasEmployeeId(employeeId);
        Specification<CompetencyEvaluationOverall> totalSpec = hasCycleId.and(hasEmployeeId);

        Specification<CompetencyEvaluationOverall> selfCompleted = GlobalSpec.selfCompleted();
        int selfOverall = competencyEvaluationOverallRepository
                .findAll(totalSpec.and(selfCompleted)).size();

        Specification<CompetencyEvaluationOverall> evaluatorCompleted = GlobalSpec.evaluatorCompleted();
        int evaluatorOverall = competencyEvaluationOverallRepository
                .findAll(totalSpec.and(evaluatorCompleted)).size();

        Specification<CompetencyEvaluationOverall> finalCompleted = GlobalSpec.finalCompleted();
        int finalOverall = competencyEvaluationOverallRepository
                .findAll(totalSpec.and(finalCompleted)).size();

        float completedPercent = ((float) (selfOverall + evaluatorOverall + finalOverall) / 3) * 100;

        return new PieChartDTO(List.of("Completed", "Uncompleted"),
                List.of(completedPercent, (100 - completedPercent)));
    }

    @Override
    public MultiBarChartDTO getSumDepartmentCompletePercent(Integer cycleId, Integer departmentId) {
        Specification<DepartmentPosition> hasDepartment = GlobalSpec.hasDepartmentId(departmentId);
        List<Position> positions = departmentPositionRepository.findAll(hasDepartment).stream().map(
                DepartmentPosition::getPosition
        ).toList();

        List<Employee> employees = employeeManagementService.getEmployeesInDepartment(departmentId);

        //Get all CompetencyEvaluationOverall of all employees have final status is agreed and get the latest cycle
        List<Float> selfData = processPositionData(positions, SELF_EVAL_LABEL_NAME, cycleId, employees);
        List<Float> managerData = processPositionData(positions, SUPERVISOR_EVAL_LABEL_NAME, cycleId, employees);

        List<MultiBarChartDataDTO> datasets = new ArrayList<>();
        datasets.add(new MultiBarChartDataDTO(SELF_EVAL_LABEL_NAME, selfData));
        datasets.add(new MultiBarChartDataDTO(SUPERVISOR_EVAL_LABEL_NAME, managerData));

        List<String> labels = positions.stream().map(Position::getPositionName).toList();
        return new MultiBarChartDTO(labels, datasets);
    }

    private List<Float> processPositionData(List<Position> positions, String type,
                                              Integer evaluateCycleId, List<Employee> employees) {
        return positions.parallelStream().map(item -> {
            List<Integer> departmentEmpIds = employees.stream()
                    .filter(emp -> Objects.equals(emp.getDepartment().getId(), item.getId()))
                    .map(Employee::getId)
                    .toList();

            return (type.equals(SELF_EVAL_LABEL_NAME))
                    ? getEmployeeInCompletedPercent(evaluateCycleId, departmentEmpIds)
                    : getEvaluatorInCompletePercent(evaluateCycleId, departmentEmpIds);
        }).toList();
    }

//    @Override
//    public MultiBarChartDTO getSumDepartmentCompletePercent(Integer cycleId, Integer departmentId) {
//        //Find all competency overall of this department
//        Specification<CompetencyEvaluationOverall> hasCycle = GlobalSpec.hasEvaluateCycleId(cycleId);
//        Specification<CompetencyEvaluationOverall> hasEmployeeDepartment = GlobalSpec.hasEmployeeDepartmentId(departmentId);
//
//        List<CompetencyEvaluationOverall> evaluations = competencyEvaluationOverallRepository
//                .findAll(hasCycle.and(hasEmployeeDepartment));
//
//        //Find all position in department
//        Specification<DepartmentPosition> hasDepartment = GlobalSpec.hasDepartmentId(departmentId);
//        List<Position> positions = departmentPositionRepository.findAll(hasDepartment).stream().map(
//                DepartmentPosition::getPosition
//        ).toList();
//
//        return getMultiBarChartResult(positions, evaluations);
//    }

//    @NotNull
//    private MultiBarChartDTO getMultiBarChartResult(List<Position> positions, List<CompetencyEvaluationOverall> evaluations) {
//        MultiBarChartDTO resultChart = new MultiBarChartDTO(List.of("Self", "Manager"), new ArrayList<>());
//
//        positions.forEach(pos -> {
//            //Find all employee belong to each position
//            Specification<Employee> eHasPosition = GlobalSpec.hasPositionId(pos.getId());
//            long totalCount = employeeRepository.findAll(eHasPosition).size();
//
//            long selfCompletedCount = evaluations.stream()
//                    .filter(evaluation -> evaluation.getEmployee().getPosition().getId().equals(pos.getId())
//                            && evaluation.getEmployeeStatus().equals(COMPETENCY_COMPLETED_STATUS))
//                    .count();
//
//            long evaluatorCompletedCount = evaluations.stream()
//                    .filter(evaluation -> evaluation.getEmployee().getPosition().getId().equals(pos.getId())
//                            && evaluation.getEvaluatorStatus().equals(COMPETENCY_COMPLETED_STATUS))
//                    .count();
//
//            float selfCompletePercent = totalCount == 0 ? 0 : ((float) selfCompletedCount / (float) totalCount) * 100;
//            float evaluatorCompletePercent = totalCount == 0 ? 0 : ((float) evaluatorCompletedCount / (float) totalCount) * 100;
//
//            List<Float> data = List.of(selfCompletePercent, evaluatorCompletePercent);
//            resultChart.getDatasets().add(new MultiBarChartDataDTO(pos.getPositionName(), data));
//        });
//
//        return resultChart;
//    }

    @Override
    public PieChartDTO getCompetencyEvaProgressPieChart(Integer cycleId, Integer departmentId) {
        List<Integer> empIdSet = employeeManagementService.getEmployeesInDepartment(departmentId)
                .stream()
                .map(Employee::getId)
                .toList();

        //get all employees who have completed evaluation
        Specification<CompetencyEvaluationOverall> hasStatusComplete = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("finalStatus"), "Completed");
        Specification<CompetencyEvaluationOverall> hasEmployeeIds = GlobalSpec.hasEmployeeIds(empIdSet);
        Specification<CompetencyEvaluationOverall> hasCycleId = GlobalSpec.hasEvaluateCycleId(cycleId);

        List<Float> datasets = new ArrayList<>();
        var completedPercent = (float) competencyEvaluationOverallRepository
                .count(hasStatusComplete.and(hasEmployeeIds).and(hasCycleId)) / empIdSet.size() * 100;
        datasets.add(completedPercent);
        datasets.add(100 - completedPercent);

        return new PieChartDTO(List.of(COMPLETED_LABEL_NAME, IN_COMPLETED_LABEL_NAME), datasets);
    }

    @Override
    public List<EmployeeStatusDTO> getCompetencyEvaluationsStatus(Integer cycleId, Integer departmentId) {
        return getEvaluationStatus(cycleId, departmentId, CompetencyEvaluationOverall.class);
    }

    @Override
    public List<EmployeeStatusDTO> getPerformanceEvaluationStatus(Integer cycleId, Integer departmentId) {
        return getEvaluationStatus(cycleId, departmentId, PerformanceEvaluationOverall.class);
    }

    private List<EmployeeStatusDTO> getEvaluationStatus(Integer cycleId, Integer departmentId, Class<?> evaluationClass) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<NameAndStatusOnly> query = cb.createQuery(NameAndStatusOnly.class);
        Root<?> root = query.from(evaluationClass);
        Join<?, Employee> empJoin = root.join("employee");

        query.multiselect(empJoin.get("id"), empJoin.get("firstName"), empJoin.get("lastName"), root.get("finalStatus"))
                .where(cb.equal(root.get("evaluateCycle").get("id"), cycleId),
                        cb.equal(empJoin.get("department").get("id"), departmentId))
                .orderBy(cb.desc(root.get("finalStatus")));

        var nameAndStatusList = entityManager.createQuery(query).getResultList();

        var empIdsSet = nameAndStatusList.stream().map(NameAndStatusOnly::id).toList();

        var profileImages = employeeDamInfoRepository.findByEmployeeIdsSetAndFileType(empIdsSet, PROFILE_IMAGE);

        return nameAndStatusList.stream()
                .map(item -> new EmployeeStatusDTO(item.id(), item.firstName(), item.lastName(), item.status(),
                        profileImages.stream()
                                .filter(profile -> profile.getEmployeeId().equals(item.id()))
                                .map(ProfileImageOnly::getUrl)
                                .findFirst()
                                .orElse(null)))
                .toList();
    }

    public double getSelfSkillAvgScore(Integer empId, Integer cycleId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Double> query = cb.createQuery(Double.class);
        Root<SkillEvaluation> root = query.from(SkillEvaluation.class);

        query.select(cb.avg(root.get("selfScore")));
        query.where(cb.equal(root.get("employee").get("id"), empId),
                cb.equal(root.get("evaluateCycle").get("id"), cycleId));

        return Optional.ofNullable(entityManager.createQuery(query).getSingleResult()).orElse(0.0);
    }


    @Override
    public MultiBarChartDTO getDepartmentCompleteComp(Integer evaluateCycleId) {
        List<Department> departments = departmentRepository.findAllByIsEvaluate(true);
        List<Employee> employees = employeeManagementService.getAllEmployeesHaveDepartment();

        //Get all CompetencyEvaluationOverall of all employees have final status is agreed and get the latest cycle
        List<Float> selfData = processDepartmentData(departments, SELF_EVAL_LABEL_NAME, evaluateCycleId, employees);
        List<Float> managerData = processDepartmentData(departments, SUPERVISOR_EVAL_LABEL_NAME, evaluateCycleId, employees);

        List<MultiBarChartDataDTO> datasets = new ArrayList<>();
        datasets.add(new MultiBarChartDataDTO(SELF_EVAL_LABEL_NAME, selfData));
        datasets.add(new MultiBarChartDataDTO(SUPERVISOR_EVAL_LABEL_NAME, managerData));

        List<String> labels = departments.stream().map(Department::getDepartmentName).toList();
        return new MultiBarChartDTO(labels, datasets);
    }

    private List<Float> processDepartmentData(List<Department> departments, String type,
                                              Integer evaluateCycleId, List<Employee> employees) {
        return departments.parallelStream().map(item -> {
            List<Integer> departmentEmpIds = employees.stream()
                    .filter(emp -> Objects.equals(emp.getDepartment().getId(), item.getId()))
                    .map(Employee::getId)
                    .toList();

            return (type.equals(SELF_EVAL_LABEL_NAME))
                    ? getEmployeeInCompletedPercent(evaluateCycleId, departmentEmpIds)
                    : getEvaluatorInCompletePercent(evaluateCycleId, departmentEmpIds);
        }).toList();
    }

    private Float getEvaluatorInCompletePercent(Integer evaluateCycleId, List<Integer> empIdSet) {
        return calculatePercentage(evaluateCycleId, empIdSet, "evaluatorStatus");
    }

    private Float getEmployeeInCompletedPercent(Integer evaluateCycleId, List<Integer> empIdSet) {
        return calculatePercentage(evaluateCycleId, empIdSet, "employeeStatus");
    }

    private Float calculatePercentage(Integer evaluateCycleId, List<Integer> empIdSet, String evalType) {
        if (empIdSet.isEmpty()) return (float) 0;
        Specification<CompetencyEvaluationOverall> hasTypeCompleted = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(evalType), "Completed");
        Specification<CompetencyEvaluationOverall> hasEmployeeIds = GlobalSpec.hasEmployeeIds(empIdSet);
        Specification<CompetencyEvaluationOverall> hasCycleId = GlobalSpec.hasEvaluateCycleId(evaluateCycleId);

        var completedCount = competencyEvaluationOverallRepository.count(hasTypeCompleted.and(hasEmployeeIds).and(hasCycleId));
        return (float) completedCount / empIdSet.size() * 100;
    }

    @Override
    public PieChartDTO getCompetencyEvalProgress(Integer evaluateCycleId) {
        List<Integer> empIdSet = employeeManagementService.getAllEmployeesEvaluate()
                .stream()
                .map(Employee::getId)
                .toList();

        //get all employees who have completed evaluation
        Specification<CompetencyEvaluationOverall> hasStatusComplete = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("finalStatus"), "Completed");
        Specification<CompetencyEvaluationOverall> hasEmployeeIds = GlobalSpec.hasEmployeeIds(empIdSet);
        Specification<CompetencyEvaluationOverall> hasCycleId = GlobalSpec.hasEvaluateCycleId(evaluateCycleId);

        List<Float> datasets = new ArrayList<>();
        var completedPercent = (float) competencyEvaluationOverallRepository
                .count(hasStatusComplete.and(hasEmployeeIds).and(hasCycleId)) / empIdSet.size() * 100;
        datasets.add(completedPercent);
        datasets.add(100 - completedPercent);

        return new PieChartDTO(List.of(COMPLETED_LABEL_NAME, IN_COMPLETED_LABEL_NAME), datasets);
    }


    @Override
    public List<HeatmapItemDTO> getHeatmapCompetency(Integer positionId, Integer evaluateCycleId) {
        List<CompetencyEvaluation> compEvaluates = fetchCompetencyEvaluations(positionId, evaluateCycleId);
        List<JobLevel> jobLevels = jobLevelRepository.findAll();
        List<Competency> competencies = competencyRepository.findAll();

        return jobLevels.stream()
                .flatMap(jobLevel -> competencies.stream()
                        .map(competency -> new Pair<>(jobLevel, competency)))
                .map(pair -> calculateAvgCompetency(compEvaluates, pair))
                .toList();
    }

    private List<CompetencyEvaluation> fetchCompetencyEvaluations(Integer positionId, Integer evaluateCycleId) {
        Specification<CompetencyEvaluation> hasCycSpec = GlobalSpec.hasEvaluateCycleId(evaluateCycleId);
        Specification<CompetencyEvaluation> hasPosSpec = employeeSpecification.hasPositionId(positionId);

        return (positionId == null || positionId == -1)
                ? competencyEvaluationRepository.findAll(hasCycSpec)
                : competencyEvaluationRepository.findAll(hasCycSpec.and(hasPosSpec));
    }

    private HeatmapItemDTO calculateAvgCompetency(List<CompetencyEvaluation> compEvaluates,
                                                  Pair<JobLevel, Competency> pair) {
        JobLevel jobLevel = pair.getFirst();
        Competency competency = pair.getSecond();

        List<CompetencyEvaluation> evaluationsHasJobLevelAndCompetency = compEvaluates.stream()
                .filter(compEva -> Objects.equals(compEva.getEmployee().getJobLevel().getId(), jobLevel.getId())
                        && Objects.equals(compEva.getCompetency().getId(), competency.getId()))
                .toList();

        float avgScore = evaluationsHasJobLevelAndCompetency.isEmpty() ? 0 :
                (float) evaluationsHasJobLevelAndCompetency.stream()
                        .map(CompetencyEvaluation::getFinalEvaluation)
                        .filter(Objects::nonNull)
                        .mapToDouble(Float::doubleValue)
                        .average()
                        .orElse(0);

        return new HeatmapItemDTO(jobLevel.getJobLevelName(), competency.getCompetencyName(), avgScore);
    }

    public static <T> TypedQuery<T> paginateQuery(TypedQuery<T> query, Pageable pageable) {
        if (pageable.isPaged()) {
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }
        return query;
    }


    @Override
    public DataItemPagingDTO getTopSkill(@Nullable Integer departmentId, @Nullable Integer employeeId,
                                         Integer evaluateCycleId, int pageNo, int pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DataItemDTO> criteriaQuery = criteriaBuilder.createQuery(DataItemDTO.class);

        Root<SkillEvaluation> skillEvaluationRoot = criteriaQuery.from(SkillEvaluation.class);
        Join<SkillEvaluation, Employee> employeeJoin = skillEvaluationRoot.join("employee");
        Join<SkillEvaluation, Skill> skillJoin = skillEvaluationRoot.join("skill");

        criteriaQuery.multiselect(
                skillJoin.get("skillName").alias("label"),
                skillEvaluationRoot.get("finalScore").as(Float.class).alias("value")
        );

        List<Integer> employeeIds = new ArrayList<>();
        if (employeeId != null) employeeIds.add(employeeId);
        if (departmentId != null) {
            employeeIds.addAll(employeeManagementService.getEmployeesInDepartment(departmentId)
                    .stream()
                    .map(Employee::getId)
                    .toList());
        }

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(skillEvaluationRoot.get("evaluateCycle").get("id"), evaluateCycleId));
        predicates.add(criteriaBuilder.equal(skillJoin.get("competency").get("id"), 7));

        if (!employeeIds.isEmpty()) {
            predicates.add(employeeJoin.get("id").in(employeeIds));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        criteriaQuery.orderBy(criteriaBuilder.desc(skillEvaluationRoot.get("finalScore")));

        TypedQuery<DataItemDTO> query = entityManager.createQuery(criteriaQuery);

        var totalItems = query.getResultList().size();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        List<DataItemDTO> results = paginateQuery(query, pageable).getResultList();

        Pagination pagination = setupPaging(totalItems, pageNo, pageSize);
        return new DataItemPagingDTO(results, pagination);
    }

    @Override
    public DataItemPagingDTO getTopKeenSkillEmployee(Integer employeeId, int pageNo, int pageSize) {
        EvaluateCycle evalLatestCycle = competencyEvaluationOverallRepository.latestEvalOByEvalCycle(employeeId);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DataItemDTO> criteriaQuery = criteriaBuilder.createQuery(DataItemDTO.class);

        Root<SkillEvaluation> sseRoot = criteriaQuery.from(SkillEvaluation.class);
        Join<SkillEvaluation, Skill> skillJoin = sseRoot.join("skill");

        criteriaQuery.multiselect(
                skillJoin.get("skillName").alias("label"),
                sseRoot.get("finalScore").as(Float.class).alias("value"));

        criteriaQuery.where(
                criteriaBuilder.and(
                        criteriaBuilder.equal(sseRoot.get("evaluateCycle").get("id"), evalLatestCycle.getId()),
                        criteriaBuilder.equal(sseRoot.get("employee").get("id"), employeeId),
                        criteriaBuilder.equal(skillJoin.get("competency").get("id"), 7)
                )
        );

        criteriaQuery.orderBy(criteriaBuilder.asc(sseRoot.get("finalScore")));

        TypedQuery<DataItemDTO> query = entityManager.createQuery(criteriaQuery);

        var totalItems = query.getResultList().size();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        List<DataItemDTO> results = paginateQuery(query, pageable).getResultList();

        Pagination pagination = setupPaging(totalItems, pageNo, pageSize);
        return new DataItemPagingDTO(results, pagination);
    }

    @Override
    public DataItemPagingDTO getTopSkillTargetEmployee(Integer employeeId, int pageNo, int pageSize, Integer evaluateCycleId) {
        Employee employee = employeeManagementService.findEmployee(employeeId);
        skillRepository.findAll();
        proficiencyLevelRepository.findAll();
        EvaluateCycle cc = competencyEvaluationOverallRepository.latestEvalOByEvalCycle(employeeId);
        if (cc == null) return null;
        Integer latestCompEvaId = cc.getId();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<SkillEvaluation> ssEvaluates = getSkillEvaluations(employeeId, latestCompEvaId);
        Page<PositionLevelSkill> skillsTargets = getSkillsTargetPage(employee, ssEvaluates, pageable);
        List<DataItemDTO> results = mapToDataItemDTO(skillsTargets);

        Pagination pagination = setupPaging(results.size(), pageNo, pageSize);
        return new DataItemPagingDTO(results, pagination);
    }

    private Page<PositionLevelSkill> getSkillsTargetPage(Employee employee, List<SkillEvaluation> ssEvaluates, Pageable pageable) {
        int newJobLevel = (employee.getJobLevel().getId() + 1) < 4 ? employee.getJobLevel().getId() + 1 : 1;
        int position = (newJobLevel == 1)
                ? ssEvaluates.stream()
                .collect(Collectors.teeing(
                        Collectors.filtering(se -> se.getSkill().getId() >= 1 && se.getSkill().getId() <= 30,
                                Collectors.averagingDouble(SkillEvaluation::getFinalScore)),
                        Collectors.filtering(se -> se.getSkill().getId() > 30,
                                Collectors.averagingDouble(SkillEvaluation::getFinalScore)),
                        (avgSoftSkills, avgHardSkills) -> avgHardSkills > avgSoftSkills ? 12 : 6))
                : employee.getPosition().getId();

        Specification<PositionLevelSkill> hasPosition = GlobalSpec.hasPositionId(position);
        Specification<PositionLevelSkill> hasJobLevel = GlobalSpec.hasJobLevelId(newJobLevel);

        Sort sort = Sort.by(Sort.Order.desc("proficiencyLevel.score"));

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        return positionLevelSkillRepository.findAll(hasPosition.and(hasJobLevel), sortedPageable);
    }

    public List<DataItemDTO> mapToDataItemDTO(Page<PositionLevelSkill> positionLevelSkills) {
        return positionLevelSkills.stream()
                .map(pls -> {
                    DataItemDTO dto = new DataItemDTO();
                    dto.setLabel(pls.getSkill().getSkillName());
                    dto.setValue(pls.getProficiencyLevel().getScore());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CurrentEvaluationDTO getCurrentEvaluation(Integer employeeId) {
        Specification<CompetencyEvaluationOverall> hasEmpId = employeeSpecification.hasEmployeeId(employeeId);
        Specification<CompetencyEvaluationOverall> hasEvalCycleIds = GlobalSpec
                .hasEvaluateCycleId(latestEvaluateCycle.getId());

        CompetencyEvaluationOverall evalOvr = competencyEvaluationOverallRepository
                .findOne(hasEmpId.and(hasEvalCycleIds))
                .orElse(null);
        return evalOvr == null
                ? new CurrentEvaluationDTO(
                latestEvaluateCycle.getEvaluateCycleName(),
                "Not Started",
                null)
                : new CurrentEvaluationDTO(evalOvr.getEvaluateCycle().getEvaluateCycleName(),
                evalOvr.getFinalStatus(), evalOvr.getLastUpdated().toString());
    }

    @Override
    public List<HistoryEvaluationDTO> getHistoryEvaluations(Integer employeeId) {
        List<Integer> evalCycleIds = evaluateCycleRepository.findAll()
                .stream()
                .map(EvaluateCycle::getId)
                .toList();
        Specification<CompetencyEvaluationOverall> hasEmployeeId = GlobalSpec.hasEmployeeId(employeeId);
        Specification<CompetencyEvaluationOverall> hasCycleIds = GlobalSpec.hasEvaluateCycleIds(evalCycleIds);

        return competencyEvaluationOverallRepository
                .findAll(hasEmployeeId.and(hasCycleIds))
                .stream()
                .map(evalOvr -> {
                    String completedDate = evalOvr.getCompletedDate() != null
                            ? evalOvr.getCompletedDate().toString()
                            : "Incomplete";

                    return new HistoryEvaluationDTO(
                            completedDate,
                            evalOvr.getEvaluateCycle().getEvaluateCycleName(),
                            evalOvr.getFinalStatus(),
                            evalOvr.getFinalAssessment()
                    );
                })
                .toList();
    }


    @Override
    public DiffPercentDTO getCompetencyDiffPercent(Integer departmentId, Integer cycleId) {
        List<Integer> employeeIds = departmentId != null
                ? employeeManagementService.getEmployeesInDepartment(departmentId)
                .stream()
                .map(Employee::getId)
                .toList()
                : new ArrayList<>();

        //Get the highest score of proficiency level
        float highestScore = proficiencyLevelRepository.findFirstByOrderByScoreDesc().getScore();

        EvaluateCycle currentCycle = evaluateCycleRepository.findById(cycleId).orElseThrow();

        float avgCurrentEvalScore = getAvgEvalScore(currentCycle.getId(), employeeIds);

        //Get previous cycle by current year - 1
        EvaluateCycle previousCycle = evaluateCycleRepository.findByYear(currentCycle.getYear() - 1);

        if (previousCycle == null)
            return new DiffPercentDTO(avgCurrentEvalScore, highestScore, (float) 100, true);

        float avgPreviousEvalScore = getAvgEvalScore(previousCycle.getId(), employeeIds);

        float diffPercentage = ((avgCurrentEvalScore - avgPreviousEvalScore) / avgPreviousEvalScore) * 100;

        return new DiffPercentDTO(avgCurrentEvalScore, highestScore, diffPercentage, diffPercentage > 0);
    }

    private float getAvgEvalScore(Integer cycleId, List<Integer> employeeIds) {
        //Get all evaluation overall of all employees have final status is agreed and get the latest cycle
        Specification<CompetencyEvaluationOverall> hasComplete = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("finalStatus"), "Completed");
        Specification<CompetencyEvaluationOverall> hasEvalCycle = GlobalSpec.hasEvaluateCycleId(cycleId);

        Specification<CompetencyEvaluationOverall> spec = employeeIds.isEmpty()
                ? hasComplete.and(hasEvalCycle)
                : hasComplete.and(hasEvalCycle).and(GlobalSpec.hasEmployeeIds(employeeIds));

        List<Float> evalScores = competencyEvaluationOverallRepository.findAll(spec)
                .stream()
                .map(CompetencyEvaluationOverall::getFinalAssessment)
                .toList();

        return (float) evalScores.stream().mapToDouble(Float::doubleValue).average().orElse(0);
    }

    @Override
    public BarChartDTO getCompetencyOverviewChart(Integer departmentId, Integer cycleId) {
        List<Integer> employeeIds = departmentId != null
                ? employeeManagementService.getEmployeesInDepartment(departmentId)
                .stream()
                .map(Employee::getId)
                .toList()
                : Collections.emptyList();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DataItemDTO> criteriaQuery = criteriaBuilder.createQuery(DataItemDTO.class);

        Root<CompetencyEvaluation> ceRoot = criteriaQuery.from(CompetencyEvaluation.class);
        Join<CompetencyEvaluation, Competency> cJoin = ceRoot.join("competency");
        Join<CompetencyEvaluation, Employee> eJoin = ceRoot.join("employee");

        criteriaQuery.multiselect(
                cJoin.get("competencyName").alias("label"),
                criteriaBuilder.avg(ceRoot.get("finalEvaluation")).as(Float.class).alias("value")
        );

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(ceRoot.get("evaluateCycle").get("id"), cycleId));

        if (!employeeIds.isEmpty()) {
            predicates.add(eJoin.get("id").in(employeeIds));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        criteriaQuery.groupBy(ceRoot.get("competency").get("id"));

        TypedQuery<DataItemDTO> query = entityManager.createQuery(criteriaQuery);
        List<DataItemDTO> results = query.getResultList();

        return new BarChartDTO("Competency", results);
    }

    @Override
    public RadarChartDTO getOverallCompetencyRadarChart(Integer employeeId, Integer evaluateCycleId) throws RuntimeException {
        Specification<CompetencyEvaluation> hasEmpId = employeeSpecification.hasEmployeeId(employeeId);
        Specification<CompetencyEvaluation> hasCycId = GlobalSpec.hasEvaluateCycleId(evaluateCycleId);

        List<CompetencyEvaluation> evaluations = competencyEvaluationRepository.findAll(hasEmpId.and(hasCycId));
        List<Competency> competencies = competencyRepository.findAll()
                .stream().sorted(Comparator.comparing(Competency::getOrdered)).toList();

        RadarDatasetDTO selfEvalData = new RadarDatasetDTO(SELF_EVAL_LABEL_NAME, new ArrayList<>());
        RadarDatasetDTO supervisorEvalData = new RadarDatasetDTO(SUPERVISOR_EVAL_LABEL_NAME, new ArrayList<>());
        RadarDatasetDTO finalEvalData = new RadarDatasetDTO(FINAL_EVAL_LABEL_NAME, new ArrayList<>());

        competencies.forEach(com -> {
            selfEvalData.getDataset().add(getCompetenciesScore(evaluations, com.getId(), "SELF"));
            supervisorEvalData.getDataset().add(getCompetenciesScore(evaluations, com.getId(), "SUPERVISOR"));
            finalEvalData.getDataset().add(getCompetenciesScore(evaluations, com.getId(), "FINAL"));
        });

        return new RadarChartDTO(competencies.stream().map(Competency::getCompetencyName).toList(),
                List.of(selfEvalData, supervisorEvalData, finalEvalData));
    }

    private Float getCompetenciesScore(List<CompetencyEvaluation> evaluations,
                                       Integer competencyId, String evaluationType) throws RuntimeException {
        return evaluations.stream()
                .filter(eva -> eva.getCompetency().getId().equals(competencyId))
                .map(eva -> switch (evaluationType) {
                    case "SELF" -> eva.getSelfEvaluation();
                    case "SUPERVISOR" -> eva.getSupervisorEvaluation();
                    case "FINAL" -> eva.getFinalEvaluation();
                    default -> null;
                })
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Evaluation type is not valid"));
    }


    @Override
    public List<EmployeeSkillMatrixDTO> getEmployeeSkillMatrix(Integer empId) {
        Employee employee = employeeManagementService.findEmployee(empId);
        skillRepository.findAll();
        proficiencyLevelRepository.findAll();
        EvaluateCycle cc = competencyEvaluationOverallRepository.latestEvalOByEvalCycle(empId);
        if (cc == null) return Collections.emptyList();
        Integer latestCompEvaId = cc.getId();

        List<Competency> competencies = competencyRepository.findAll();

        //Setup data:
        List<PositionLevelSkill> listPoLSs = getPositionLSkills(employee.getPosition().getId(), employee.getJobLevel().getId());
        List<SkillEvaluation> ssEvaluates = getSkillEvaluations(empId, latestCompEvaId);
        List<PositionLevelSkill> skillsTargets = getSkillsTarget(employee, ssEvaluates);

        return competencies.stream()
                .map(competency -> {
                    List<EmployeeSkillMatrixDTO> children =
                            handleChildren(competency.getId(), listPoLSs, ssEvaluates, skillsTargets);
                    SkillMatrixDataDTO smData = calculateSkillMatrixData(competency.getCompetencyName(), children);
                    return new EmployeeSkillMatrixDTO(smData, children);
                })
                .toList();
    }

    private List<PositionLevelSkill> getSkillsTarget(Employee employee, List<SkillEvaluation> ssEvaluates) {
        int newJobLevel = (employee.getJobLevel().getId() + 1) < 4 ? employee.getJobLevel().getId() + 1 : 1;
        int position = (newJobLevel == 1)
                ? ssEvaluates.stream()
                .collect(Collectors.teeing(
                        Collectors.filtering(se -> se.getSkill().getId() >= 1 && se.getSkill().getId() <= 30,
                                Collectors.averagingDouble(SkillEvaluation::getFinalScore)),
                        Collectors.filtering(se -> se.getSkill().getId() > 30,
                                Collectors.averagingDouble(SkillEvaluation::getFinalScore)),
                        (avgSoftSkills, avgHardSkills) -> avgHardSkills > avgSoftSkills ? 12 : 6))
                : employee.getPosition().getId();

        Specification<PositionLevelSkill> hasPosition = GlobalSpec.hasPositionId(position);
        Specification<PositionLevelSkill> hasJobLevel = GlobalSpec.hasJobLevelId(newJobLevel);

        return positionLevelSkillRepository.findAll(hasPosition.and(hasJobLevel));
    }

    private List<PositionLevelSkill> getPositionLSkills(Integer positionId, Integer jobLevelId) {
        Specification<PositionLevelSkill> hasPositionId = GlobalSpec.hasPositionId(positionId);
        Specification<PositionLevelSkill> hasJobLevelId = GlobalSpec.hasJobLevelId(jobLevelId);
        return positionLevelSkillRepository.findAll(hasPositionId.and(hasJobLevelId));
    }

    private List<SkillEvaluation> getSkillEvaluations(Integer employeeId, Integer cycleId) {
        Specification<SkillEvaluation> hasEmployeeId = GlobalSpec.hasEmployeeId(employeeId);
        Specification<SkillEvaluation> hasCycleId = GlobalSpec.hasEvaluateCycleId(cycleId);
        return skillEvaluationRepository.findAll(hasEmployeeId.and(hasCycleId));
    }

    private List<EmployeeSkillMatrixDTO> handleChildren(Integer competencyId,
                                                        List<PositionLevelSkill> listPoLSs,
                                                        List<SkillEvaluation> ssEvaluates,
                                                        List<PositionLevelSkill> ssTargets) {
        List<PositionLevelSkill> listPoSsFilter = listPoLSs
                .stream()
                .filter(item -> Objects.equals(item.getSkill().getCompetency().getId(), competencyId))
                .toList();

        List<Integer> listSkillIds = listPoSsFilter.stream().map(item -> item.getSkill().getId()).toList();
        List<SkillEvaluation> ssEvaluatesFilter = ssEvaluates
                .stream()
                .filter(item -> listSkillIds.contains(item.getSkill().getId()))
                .toList();
        List<PositionLevelSkill> ssTargetsFilter = ssTargets
                .stream()
                .filter(item -> listSkillIds.contains(item.getSkill().getId()))
                .toList();

        return listPoSsFilter.stream()
                .map(item -> {
                    SkillMatrixDataDTO smDataChild = calculateSkillMatrixDataChild(item, ssEvaluatesFilter, ssTargetsFilter);
                    return new EmployeeSkillMatrixDTO(smDataChild, null);
                })
                .toList();
    }

    private SkillMatrixDataDTO calculateSkillMatrixDataChild(PositionLevelSkill item,
                                                             List<SkillEvaluation> ssEvaluates,
                                                             List<PositionLevelSkill> ssTargets) {
        SkillEvaluation ssEva = ssEvaluates.stream()
                .filter(ssEvaluate -> ssEvaluate.getSkill().getId().equals(item.getSkill().getId()))
                .findFirst()
                .orElse(null);

        PositionLevelSkill ssTarget = ssTargets.stream()
                .filter(ssT -> ssT.getSkill().getId().equals(item.getSkill().getId()))
                .findFirst()
                .orElse(null);

        return ssEva != null && ssTarget != null ? new SkillMatrixDataDTO(
                item.getSkill().getSkillName(),
                (double) ssTarget.getProficiencyLevel().getScore(),
                (double) ssEva.getFinalScore(),
                (double) ssEva.getSelfScore(),
                (double) ssEva.getEvaluatorScore(),
                ((double) ssEva.getFinalScore() / (double) ssTarget.getProficiencyLevel().getScore()) * 100)
                : null;
    }

    private SkillMatrixDataDTO calculateSkillMatrixData(String competencyName, List<EmployeeSkillMatrixDTO> children) {
        int totalSkill = children.size();

        double targetScore = children.stream()
                .map(EmployeeSkillMatrixDTO::getData)
                .filter(Objects::nonNull)
                .mapToDouble(SkillMatrixDataDTO::getTargetSkillLevel)
                .sum();

        double totalScore = children.stream()
                .map(EmployeeSkillMatrixDTO::getData)
                .filter(Objects::nonNull)
                .mapToDouble(SkillMatrixDataDTO::getSkillLevelTotal)
                .sum();

        double selfScore = children.stream()
                .map(EmployeeSkillMatrixDTO::getData)
                .filter(Objects::nonNull)
                .mapToDouble(SkillMatrixDataDTO::getSkillLevelSelf)
                .sum();

        double evaluatorScore = children.stream()
                .map(EmployeeSkillMatrixDTO::getData)
                .filter(Objects::nonNull)
                .mapToDouble(SkillMatrixDataDTO::getSkillLevelManager)
                .sum();

        double competencyScore = children.stream()
                .map(EmployeeSkillMatrixDTO::getData)
                .filter(Objects::nonNull)
                .mapToDouble(SkillMatrixDataDTO::getCompetencyLevel)
                .sum();


        return new SkillMatrixDataDTO(
                competencyName,
                targetScore / totalSkill,
                totalScore / totalSkill,
                selfScore / totalSkill,
                evaluatorScore / totalSkill,
                competencyScore / totalSkill
        );
    }

    @Override
    public RadarChartDTO getCompetencyRadarChart(List<Integer> evaluateCycleIds, Integer departmentId) {
        List<Competency> competencies = competencyRepository.findAll();
        List<EvaluateCycle> evaluateCycles = evaluateCycleRepository.findAll();
        List<CompetencyEvaluation> competencyEvaluates = findByCyclesAndDepartment(evaluateCycleIds, departmentId);
        if (competencyEvaluates.isEmpty()) return null;
        proficiencyLevelRepository.findAll();

        List<Pair<Integer, Integer>> pairItems = createPairItems(evaluateCycleIds, competencies);

        List<RadarValueDTO> avgCompetencies = calculateAverageCompetencies(pairItems, competencyEvaluates);

        List<RadarDatasetDTO> listDataset = createRadarDataset(evaluateCycleIds,
                competencies,
                avgCompetencies,
                evaluateCycles);

        List<String> labels = competencies.stream().map(Competency::getCompetencyName).toList();
        return new RadarChartDTO(labels, listDataset);
    }

    private List<CompetencyEvaluation> findByCyclesAndDepartment(List<Integer> evaluateCycleIds, Integer departmentId) {
        Specification<CompetencyEvaluation> hasEvalCycleIds = GlobalSpec.hasEvaluateCycleIds(evaluateCycleIds);
        Specification<CompetencyEvaluation> hasEmployeeDepartment = GlobalSpec.hasEmployeeDepartmentId(departmentId);
        Specification<CompetencyEvaluation> hasFinalEvaluation = GlobalSpec.hasFinalEvaluationNotNull();
        return competencyEvaluationRepository.findAll(hasEvalCycleIds.and(hasEmployeeDepartment).and(hasFinalEvaluation));
    }

    private List<Pair<Integer, Integer>> createPairItems(List<Integer> inputIds, List<Competency> competencies) {
        return inputIds.stream()
                .flatMap(input -> competencies.stream().map(competency -> new Pair<>(input, competency.getId())))
                .toList();
    }

    private List<RadarValueDTO> calculateAverageCompetencies(List<Pair<Integer, Integer>> pairItems,
                                                             List<CompetencyEvaluation> competencyEvaluates) {
        return pairItems.stream().map(pair -> {
            var cycleId = pair.getFirst();
            var competencyId = pair.getSecond();
            List<CompetencyEvaluation> compEvaluate = competencyEvaluates.stream()
                    .filter(compEva -> Objects.equals(compEva.getEvaluateCycle().getId(), cycleId)
                            && compEva.getCompetency().getId().equals(competencyId))
                    .toList();
            float avgScore = compEvaluate.isEmpty() ? 0
                    : (float) compEvaluate.stream()
                    .map(CompetencyEvaluation::getFinalEvaluation)
                    .mapToDouble(Float::doubleValue)
                    .average()
                    .orElse(0);
            return new RadarValueDTO(cycleId, competencyId, avgScore);
        }).toList();
    }

    private List<RadarDatasetDTO> createRadarDataset(List<Integer> evaluateCycleIds,
                                                     List<Competency> competencies,
                                                     List<RadarValueDTO> avgCompetencies,
                                                     List<EvaluateCycle> evaluateCycles) {
        return evaluateCycleIds.stream().map(cycle -> {
            List<Float> listScore = competencies.stream()
                    .map(competency -> avgCompetencies.stream()
                            .filter(avgCompetency -> avgCompetency.getCompetencyId().equals(competency.getId()) &&
                                    avgCompetency.getInputId().equals(cycle))
                            .findFirst()
                            .map(RadarValueDTO::getAverage)
                            .orElse(null))
                    .toList();
            return new RadarDatasetDTO(
                    evaluateCycles.stream()
                            .filter(evalCycle -> Objects.equals(evalCycle.getId(), cycle))
                            .findFirst()
                            .orElseThrow()
                            .getEvaluateCycleName(),
                    listScore);
        }).toList();
    }


    private Float getAvgSkillScore(Integer employeeId, Integer cycleId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Float> query = criteriaBuilder.createQuery(Float.class);
        Root<SkillEvaluation> root = query.from(SkillEvaluation.class);
        Join<SkillEvaluation, ProficiencyLevel> proficencyJoin = root.join("finalProficiencyLevel");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(entityManager.getCriteriaBuilder().equal(root.get("employee").get("id"), employeeId));
        predicates.add(entityManager.getCriteriaBuilder().equal(root.get("evaluateCycle").get("id"), cycleId));

        query.multiselect(criteriaBuilder.avg(proficencyJoin.get("finalAssessment"))).where(predicates.toArray(new Predicate[]{}));

        return entityManager.createQuery(query).getSingleResult();
    }

    private Float getAvgBaselineSkill(Integer positionId, Integer jobLevelId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Float> query = criteriaBuilder.createQuery(Float.class);
        Root<PositionLevelSkill> root = query.from(PositionLevelSkill.class);
        Join<PositionLevelSkill, ProficiencyLevel> proficencyJoin = root.join("proficiencyLevel");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(entityManager.getCriteriaBuilder().equal(root.get("position").get("id"), positionId));
        predicates.add(entityManager.getCriteriaBuilder().equal(root.get("jobLevel").get("id"), jobLevelId));

        query.multiselect(criteriaBuilder.avg(proficencyJoin.get("finalAssessment"))).where(predicates.toArray(new Predicate[]{}));

        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public EmployeeRatingPagination getCompetencyRating(@Nullable Integer departmentId, Integer cycleId,
                                                        Integer pageNo, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EmployeeRatingDTO> criteriaQuery = criteriaBuilder.createQuery(EmployeeRatingDTO.class);

        Root<CompetencyEvaluationOverall> ceoRoot = criteriaQuery.from(CompetencyEvaluationOverall.class);
        Join<CompetencyEvaluationOverall, Employee> employeeJoin = ceoRoot.join("employee");

        criteriaQuery.multiselect(
                employeeJoin.get("id"),
                employeeJoin.get("firstName"),
                employeeJoin.get("lastName"),
                employeeJoin.get("email").alias("profileImgUrl"),
                ceoRoot.get("finalAssessment").alias("rating")
        );

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(ceoRoot.get("evaluateCycle").get("id"), cycleId));
        predicates.add(criteriaBuilder.isNotNull(ceoRoot.get("finalAssessment")));

        if (departmentId != null) {
            predicates.add(criteriaBuilder.equal(employeeJoin.get("department").get("id"), departmentId));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        criteriaQuery.orderBy(criteriaBuilder.desc(ceoRoot.get("finalAssessment")));

        List<EmployeeRatingDTO> ratingDTOS = entityManager.createQuery(criteriaQuery).getResultList();
        ratingDTOS.forEach(item -> item.setProfileImgUrl(employeeManagementService.getProfilePicture(item.getId())));

        Pagination pagination = setupPaging(ratingDTOS.size(), pageNo, pageSize);
        return new EmployeeRatingPagination(ratingDTOS, pagination);
    }

    @Override
    public List<HeatmapItemDTO> getDepartmentSkillHeatmap(Integer cycleId,
                                                          List<Integer> employeeIds, List<Integer> competencyIds) {
        proficiencyLevelRepository.findAll();

        List<SimpleItemDTO> employees = getEmployeeSimpleItemDTOS(employeeIds);

        List<SimpleItemDTO> skills = getSkillSimpleItemDTOS(competencyIds);

        List<SkillEvaluation> ssEvaluates = getSSEvalByEmployeeAndCycle(cycleId, employeeIds);

        return employees.stream()
                .flatMap(employee -> skills.stream()
                        .map(skill -> new Pair<>(employee, skill)))
                .map(pair -> getSkillScore(ssEvaluates, pair))
                .toList();
    }

    @NotNull
    private List<SimpleItemDTO> getEmployeeSimpleItemDTOS(List<Integer> employeeIds) {
        Specification<Employee> hasIds = GlobalSpec.hasIds(employeeIds);
        return employeeRepository.findAll(hasIds)
                .stream()
                .map(employee -> new SimpleItemDTO(employee.getId(), employee.getFullName()))
                .toList();
    }

    @NotNull
    private List<SimpleItemDTO> getSkillSimpleItemDTOS(List<Integer> competencyIds) {
        Specification<Skill> hasCompetencyIds = GlobalSpec.hasCompetencyIds(competencyIds);
        return skillRepository.findAll(hasCompetencyIds)
                .stream()
                .map(skill -> new SimpleItemDTO(skill.getId(), skill.getSkillName()))
                .toList();
    }

    @NotNull
    private List<SkillEvaluation> getSSEvalByEmployeeAndCycle(Integer evaluateCycleId, List<Integer> employeeIds) {
        Specification<SkillEvaluation> hasEvaluateCycleId = GlobalSpec.hasEvaluateCycleId(evaluateCycleId);
        Specification<SkillEvaluation> hasEmployeeIds = GlobalSpec.hasEmployeeIds(employeeIds);

        return skillEvaluationRepository.findAll(hasEvaluateCycleId.and(hasEmployeeIds));
    }

    private HeatmapItemDTO getSkillScore(List<SkillEvaluation> ssEvaluates,
                                         Pair<SimpleItemDTO, SimpleItemDTO> pair) {
        SimpleItemDTO employee = pair.getFirst();
        SimpleItemDTO skill = pair.getSecond();

        SkillEvaluation evaluationsHasEmployeeAndSkill = ssEvaluates.stream()
                .filter(ssEva -> ssEva.getEmployee().getId() == employee.getId()
                        && Objects.equals(ssEva.getSkill().getId(), skill.getId())).findFirst().orElse(null);

        float score = (evaluationsHasEmployeeAndSkill == null || evaluationsHasEmployeeAndSkill.getFinalScore() == null)
                ? 0
                : evaluationsHasEmployeeAndSkill.getFinalScore();

        return new HeatmapItemDTO(employee.getName(), skill.getName(), score);
    }

    @Override
    public RadarChartDTO getDepartmentCompetencyGap(Integer cycleId, List<Integer> employeeIds) {
        List<Competency> competencies = competencyRepository.findAll();
        List<Employee> employees = employeeRepository.findAll(GlobalSpec.hasIds(employeeIds));
        List<CompetencyEvaluation> competencyEvaluates = findByCycleAndEmployees(cycleId, employeeIds);
        proficiencyLevelRepository.findAll();

        List<Pair<Integer, Integer>> pairItems = createPairItems(employeeIds, competencies);

        List<RadarValueDTO> competencyScores = getCompetencyScores(pairItems, competencyEvaluates);

        List<RadarDatasetDTO> listDataset = createDataset(employeeIds, competencies, competencyScores, employees);

        List<String> labels = competencies.stream().map(Competency::getCompetencyName).toList();
        return new RadarChartDTO(labels, listDataset);
    }

    private List<RadarDatasetDTO> createDataset(List<Integer> employeeIds, List<Competency> competencies,
                                                List<RadarValueDTO> competencyScores, List<Employee> employees) {
        return employeeIds.stream().map(employeeId -> {
            List<Float> listScore = competencies.stream()
                    .map(competency -> competencyScores.stream()
                            .filter(score -> score.getCompetencyId().equals(competency.getId()) &&
                                    score.getInputId().equals(employeeId))
                            .findFirst()
                            .map(RadarValueDTO::getAverage)
                            .orElse(null))
                    .toList();
            return new RadarDatasetDTO(
                    employees.stream()
                            .filter(employee -> Objects.equals(employee.getId(), employeeId))
                            .findFirst()
                            .orElseThrow()
                            .getFullName(),
                    listScore);
        }).toList();
    }

    private List<RadarValueDTO> getCompetencyScores(List<Pair<Integer, Integer>> pairItems,
                                                    List<CompetencyEvaluation> competencyEvaluates) {
        return pairItems.stream().map(pair -> {
            var employeeId = pair.getFirst();
            var competencyId = pair.getSecond();
            CompetencyEvaluation compEvaluate = competencyEvaluates.stream()
                    .filter(compEva -> Objects.equals(compEva.getEmployee().getId(), employeeId)
                            && compEva.getCompetency().getId().equals(competencyId))
                    .findFirst().orElse(null);
            Float score = (compEvaluate == null || compEvaluate.getFinalEvaluation() == null)
                    ? 0
                    : compEvaluate.getFinalEvaluation();
            return new RadarValueDTO(employeeId, competencyId, score);
        }).toList();
    }

    private List<CompetencyEvaluation> findByCycleAndEmployees(Integer evaluateCycleId, List<Integer> employeeIds) {
        Specification<CompetencyEvaluation> hasEvaluateCycle = GlobalSpec.hasEvaluateCycleId(evaluateCycleId);
        Specification<CompetencyEvaluation> hasEmployeeDepartment = GlobalSpec.hasEmployeeIds(employeeIds);
        return competencyEvaluationRepository.findAll(hasEvaluateCycle.and(hasEmployeeDepartment));
    }

    @Override
    public String evaluateCyclePeriod(Integer evaluateCycleId) {
        EvaluateCycle cycle = evaluateCycleRepository.findAll(GlobalSpec.hasId(evaluateCycleId))
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cycle not found"));
        return String.format("%s - %s", cycle.getStartDate(), cycle.getDueDate());
    }

    @Transactional
    @Override
    public List<TimeLine> createCompetencyProcess(EvaluationProcessInput input) throws ParseException {
        List<EvaluateTimeLine> comTimeLines = input.getTimeLines()
                .stream()
                .map(tl -> modelMapper.map(tl, EvaluateTimeLine.class))
                .toList();

        EvaluateCycle cycle = evaluateCycleRepository.findAll(GlobalSpec.hasId(input.getCycleId()))
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cycle not found"));

        comTimeLines.forEach(tl -> tl.setEvaluateCycle(cycle));

        evaluateTimeLineRepository.saveAll(comTimeLines);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        cycle.setInitialDate(dateFormat.parse(input.getInitialDate()));
        evaluateCycleRepository.save(cycle);

        return modelMapper.map(comTimeLines, new TypeToken<List<TimeLine>>() {
        }.getType());
    }

    @Override
    public EmployeeEvaProgressPaging getTrackEvaluationProgress(Integer evaluateCycleId, Integer pageNo, Integer pageSize) {
        employeeRepository.findAll();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Specification<CompetencyEvaluationOverall> hasCycleId = GlobalSpec.hasEvaluateCycleId(evaluateCycleId);
        Page<CompetencyEvaluationOverall> compEvalOvr = competencyEvaluationOverallRepository.findAll(hasCycleId, pageable);
        List<EmployeeEvaProgress> evaProgresses = compEvalOvr
                .map(ceo -> EmployeeEvaProgress.builder()
                        .employeeId(ceo.getEmployee().getId())
                        .name(ceo.getEmployee().getFullName())
                        .image(employeeManagementService.getProfilePicture(ceo.getEmployee().getId()))
                        .selfStatus(ceo.getEmployeeStatus())
                        .evaluatorStatus(ceo.getEvaluatorStatus())
                        .finalStatus(ceo.getFinalStatus())
                        .build())
                .toList();

        Pagination pagination = setupPaging(evaProgresses.size(), pageNo, pageSize);

        return new EmployeeEvaProgressPaging(evaProgresses, pagination);
    }

    @Override
    public List<TreeSimpleData> getEvaluateSkillForm(Integer employeeId) {
        skillRepository.findAll();
        List<Competency> competencies = competencyRepository.findAll();
        Integer positionId = employeeRepository.findAll(GlobalSpec.hasId(employeeId))
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Employee not found"))
                .getPosition()
                .getId();
        List<Skill> skills = positionLevelSkillRepository.findAll(GlobalSpec.hasPositionId(positionId))
                .stream().map(PositionLevelSkill::getSkill).toList();

        return competencies.stream()
                .map(c -> {
                    List<TreeSimpleData> childrenSS = skills.stream()
                            .filter(ss -> ss.getCompetency().getId().equals(c.getId()))
                            .map(ss -> new TreeSimpleData(ss.getId(), ss.getSkillName(), null))
                            .toList();
                    return new TreeSimpleData(c.getId(), c.getCompetencyName(), childrenSS);
                })
                .toList();
    }

    @Override
    public List<CompetencyGroupDTO> getCompetencyGroups() {
        List<CompetencyGroup> competencyGroups = competencyGroupRepository.findAll();
        List<Competency> competencies = competencyRepository.findAll();
        return competencyGroups
                .stream()
                .map(cg -> {
                    List<Integer> competencyIds = competencies
                            .stream()
                            .filter(c -> c.getCompetencyGroup().getId().equals(cg.getId()))
                            .map(Competency::getId)
                            .toList();
                    return CompetencyGroupDTO.builder()
                            .id(cg.getId())
                            .competencyGroupName(cg.getCompetencyGroupName())
                            .weight(cg.getWeight())
                            .competencyIds(competencyIds)
                            .build();
                })
                .toList();
    }

    @Override
    public List<EvaluationResult> getEvaluationResult(Integer employeeId, Integer evaluateCycleId) {
        Specification<SkillEvaluation> hasEmployeeId = GlobalSpec.hasEmployeeId(employeeId);
        Specification<SkillEvaluation> hasCycleId = GlobalSpec.hasEvaluateCycleId(evaluateCycleId);

        List<SkillEvaluation> ssEvaluates = skillEvaluationRepository.findAll(hasCycleId.and(hasEmployeeId));

        return ssEvaluates.stream().map(ssE -> EvaluationResult.builder()
                .skillId(ssE.getSkill().getId())
                .selfEvaluation(ssE.getSelfScore())
                .evaluatorEvaluation(ssE.getEvaluatorScore())
                .build()).toList();
    }

    @Override
    @Transactional
    public Boolean createSelfCompetencyEvaluation(CompetencyEvaluationInput input) {
        Specification<SkillEvaluation> hasEmployeeId = GlobalSpec.hasEmployeeId(input.getEmployeeId());
        Specification<SkillEvaluation> hasCycleId = GlobalSpec.hasEvaluateCycleId(input.getEvaluateCycleId());

        List<SkillEvaluation> ssEvaluates = !skillEvaluationRepository.findAll(hasCycleId.and(hasEmployeeId)).isEmpty()
                ? skillEvaluationRepository.findAll(hasCycleId.and(hasEmployeeId)).stream()
                .peek(sse -> sse.setSelfScore(input.getSkillScores().stream()
                        .filter(ssScore -> ssScore.getSkillId().equals(sse.getSkill().getId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Skill set not found"))
                        .getScore()))
                .toList()

                : input.getSkillScores().stream()
                .map(ssScore -> SkillEvaluation.builder()
                        .evaluateCycle(new EvaluateCycle(input.getEvaluateCycleId()))
                        .employee(new Employee(input.getEmployeeId()))
                        .skill(new Skill(ssScore.getSkillId()))
                        .selfScore(ssScore.getScore())
                        .build())
                .toList();

        setSelfEvaluationStatus(input);

        return !skillEvaluationRepository.saveAll(ssEvaluates).isEmpty();
    }

    private void setSelfEvaluationStatus(CompetencyEvaluationInput input) {
        Specification<CompetencyEvaluationOverall> hasEmployeeId = GlobalSpec.hasEmployeeId(input.getEmployeeId());
        Specification<CompetencyEvaluationOverall> hasCycleId = GlobalSpec.hasEvaluateCycleId(input.getEvaluateCycleId());
        CompetencyEvaluationOverall ceo = competencyEvaluationOverallRepository.findAll(hasEmployeeId.and(hasCycleId))
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Competency evaluation not found"));

        ceo.setLastUpdated(new Date());
        if (Boolean.TRUE.equals(input.getIsSubmitted())) ceo.setEmployeeStatus("Completed");

        competencyEvaluationOverallRepository.save(ceo);
    }

    @Override
    @Transactional
    public Boolean createEvaluatorCompetencyEvaluation(CompetencyEvaluationInput input) {
        Specification<SkillEvaluation> hasEmployeeId = GlobalSpec.hasEmployeeId(input.getEmployeeId());
        Specification<SkillEvaluation> hasCycleId = GlobalSpec.hasEvaluateCycleId(input.getEvaluateCycleId());

        List<SkillEvaluation> ssEvaluates = !skillEvaluationRepository.findAll(hasCycleId.and(hasEmployeeId)).isEmpty()
                ? skillEvaluationRepository.findAll(hasCycleId.and(hasEmployeeId)).stream()
                .peek(sse -> sse.setEvaluatorScore(input.getSkillScores().stream()
                        .filter(ssScore -> ssScore.getSkillId().equals(sse.getSkill().getId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Skill set not found"))
                        .getScore()))
                .toList()

                : input.getSkillScores().stream()
                .map(ssScore -> SkillEvaluation.builder()
                        .evaluateCycle(new EvaluateCycle(input.getEvaluateCycleId()))
                        .employee(new Employee(input.getEmployeeId()))
                        .skill(new Skill(ssScore.getSkillId()))
                        .evaluatorScore(ssScore.getScore())
                        .build())
                .toList();

        setEvaluatorEvaluationStatus(input);

        return !skillEvaluationRepository.saveAll(ssEvaluates).isEmpty();
    }

    private void setEvaluatorEvaluationStatus(CompetencyEvaluationInput input) {
        Specification<CompetencyEvaluationOverall> hasEmployeeId = GlobalSpec.hasEmployeeId(input.getEmployeeId());
        Specification<CompetencyEvaluationOverall> hasCycleId = GlobalSpec.hasEvaluateCycleId(input.getEvaluateCycleId());
        CompetencyEvaluationOverall ceo = competencyEvaluationOverallRepository.findAll(hasEmployeeId.and(hasCycleId))
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Competency evaluation not found"));

        ceo.setLastUpdated(new Date());
        if (Boolean.TRUE.equals(input.getIsSubmitted())) ceo.setEvaluatorStatus("Completed");

        competencyEvaluationOverallRepository.save(ceo);
    }

    @Override
    public Boolean createFinalCompetencyEvaluation(CompetencyEvaluationInput input) {
        Specification<SkillEvaluation> hasEmployeeId = GlobalSpec.hasEmployeeId(input.getEmployeeId());
        Specification<SkillEvaluation> hasCycleId = GlobalSpec.hasEvaluateCycleId(input.getEvaluateCycleId());

        List<SkillEvaluation> ssEvaluates = !skillEvaluationRepository.findAll(hasCycleId.and(hasEmployeeId)).isEmpty()
                ? skillEvaluationRepository.findAll(hasCycleId.and(hasEmployeeId)).stream()
                .peek(sse -> sse.setFinalScore(input.getSkillScores().stream()
                        .filter(ssScore -> ssScore.getSkillId().equals(sse.getSkill().getId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Skill set not found"))
                        .getScore()))
                .toList()

                : input.getSkillScores().stream()
                .map(ssScore -> SkillEvaluation.builder()
                        .evaluateCycle(new EvaluateCycle(input.getEvaluateCycleId()))
                        .employee(new Employee(input.getEmployeeId()))
                        .skill(new Skill(ssScore.getSkillId()))
                        .finalScore(ssScore.getScore())
                        .build())
                .toList();

        setFinalEvaluationStatus(input);

        return !skillEvaluationRepository.saveAll(ssEvaluates).isEmpty();
    }

    private void setFinalEvaluationStatus(CompetencyEvaluationInput input) {
        Specification<CompetencyEvaluationOverall> hasEmployeeId = GlobalSpec.hasEmployeeId(input.getEmployeeId());
        Specification<CompetencyEvaluationOverall> hasCycleId = GlobalSpec.hasEvaluateCycleId(input.getEvaluateCycleId());
        CompetencyEvaluationOverall ceo = competencyEvaluationOverallRepository.findAll(hasEmployeeId.and(hasCycleId))
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Competency evaluation not found"));

        ceo.setLastUpdated(new Date());
        if (Boolean.TRUE.equals(input.getIsSubmitted())) {
            ceo.setFinalStatus("Completed");
            ceo.setCompletedDate(new Date());
            ceo.setFinalAssessment(input.getScore());
        }

        competencyEvaluationOverallRepository.save(ceo);
    }

    @Override
    public List<CycleOverallDTO> getCyclesOverall() {
        List<EvaluateCycle> cycles = getEvaluateCycles();
        return cycles.stream().map(c -> {
            PieChartDTO completedEvaluate = getCompetencyEvalProgress(c.getId());
            PieChartDTO competencyOverall = getCompetencyPieChartOverall(c);
            PieChartDTO performanceOverall = performanceService.getPerformancePieChartOverall(c);
            return CycleOverallDTO.builder()
                    .name(c.getEvaluateCycleName())
                    .status(c.getStatus())
                    .startDate(c.getStartDate().toString())
                    .dueDate(c.getDueDate().toString())
                    .completedEvaluate(completedEvaluate)
                    .competencyOverall(competencyOverall)
                    .performanceOverall(performanceOverall)
                    .build();
        }).toList();
    }

    public List<EvaluateCycle> getEvaluateCycles() {
        //Sort by initialDate DESC
        Sort sort = Sort.by("initialDate").descending();
        return evaluateCycleRepository.findAll(sort);
    }

    private PieChartDTO getCompetencyPieChartOverall(EvaluateCycle cycle) {
        if (cycle.getStatus().equals("Not Start")) return null;
        List<Competency> competencies = competencyRepository.findAll();
        Specification<CompetencyEvaluation> hasCycleId = GlobalSpec.hasEvaluateCycleId(cycle.getId());
        List<CompetencyEvaluation> ces = competencyEvaluationRepository.findAll(hasCycleId);

        List<String> labels = competencies.stream().map(Competency::getCompetencyName).toList();
        List<Float> datasets = competencies.stream()
                .map(c -> {
                    long count = ces.stream()
                            .filter(ce -> ce.getCompetency().getId().equals(c.getId()))
                            .count();

                    return calculatePercent((int) count, ces.size());
                })
                .toList();

        return new PieChartDTO(labels, datasets);
    }

    private float calculatePercent(int number, int total) {
        return ((float) number / total) * 100;
    }


}