package com.hrms.careerpathmanagement.services.impl;

import com.hrms.careerpathmanagement.dto.*;
import com.hrms.careerpathmanagement.models.*;
import com.hrms.careerpathmanagement.repositories.*;
import com.hrms.careerpathmanagement.services.CareerManagementService;
import com.hrms.careerpathmanagement.services.CompetencyService;
import com.hrms.careerpathmanagement.specification.CareerSpecification;
import com.hrms.careerpathmanagement.specification.CompetencySpecification;
import com.hrms.employeemanagement.dto.EmployeeRatingDTO;
import com.hrms.employeemanagement.dto.EmployeeStatusDTO;
import com.hrms.employeemanagement.dto.SimpleItemDTO;
import com.hrms.employeemanagement.projection.NameAndStatusOnly;
import com.hrms.employeemanagement.dto.pagination.EmployeeRatingPagination;
import com.hrms.employeemanagement.dto.pagination.EmployeeStatusPagination;
import com.hrms.employeemanagement.models.*;
import com.hrms.employeemanagement.specification.EmployeeSpecification;
import com.hrms.global.GlobalSpec;
import com.hrms.global.dto.*;
import com.hrms.global.paging.Pagination;
import com.hrms.employeemanagement.repositories.*;
import com.hrms.employeemanagement.services.EmployeeManagementService;
import com.hrms.performancemanagement.model.PerformanceCycle;
import com.hrms.performancemanagement.model.PerformanceEvaluation;
import com.hrms.performancemanagement.repositories.PerformanceCycleRepository;
import com.hrms.performancemanagement.specification.PerformanceSpecification;
import com.mysema.commons.lang.Pair;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Stream;

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
    static final String COMPETENCY_COMPLETED_STATUS = "Agreed";
    static final String PROFILE_IMAGE = "PROFILE_IMAGE";


    private final CompetencyEvaluationRepository competencyEvaluationRepository;
    private final EmployeeRepository employeeRepository;
    private final CompetencyTimeLineRepository competencyTimeLineRepository;
    private final SkillSetEvaluationRepository skillSetEvaluationRepository;
    private final SkillSetTargetRepository skillSetTargetRepository;
    private final PositionSkillSetRepository positionSkillSetRepository;
    private final CompetencyRepository competencyRepository;
    private final CompetencyCycleRepository competencyCycleRepository;
    private final ProficiencyLevelRepository proficiencyLevelRepository;
    private final EvaluationOverallRepository evaluationOverallRepository;
    private final SkillSetRepository skillSetRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeManagementService employeeManagementService;
    private final JobLevelRepository jobLevelRepository;
    private final PositionJobLevelSkillSetRepository positionLevelSkillSetRepository;
    private final PerformanceEvaluationRepository performanceEvaluationRepository;
    private final PositionDepartmentRepository positionDepartmentRepository;
    private final EmployeeDamInfoRepository employeeDamInfoRepository;
    private final CareerSpecification careerSpecification;
    private final EmployeeSpecification employeeSpecification;
    private final CompetencySpecification competencySpecification;
    private final PerformanceCycleRepository performanceCycleRepository;
    private final PerformanceSpecification performanceSpecification;
    private final CareerManagementService careerManagementService;
    private CompetencyCycle latestCompCycle;
    private PerformanceCycle latestPerformCycle;

    @Autowired
    public CompetencyServiceImpl(CompetencyEvaluationRepository competencyEvaluationRepository,
                                 EmployeeRepository employeeRepository,
                                 CompetencyTimeLineRepository competencyTimeLineRepository,
                                 SkillSetEvaluationRepository skillSetEvaluationRepository,
                                 SkillSetTargetRepository skillSetTargetRepository,
                                 PositionSkillSetRepository positionSkillSetRepository,
                                 CompetencyRepository competencyRepository,
                                 CompetencyCycleRepository competencyCycleRepository,
                                 ProficiencyLevelRepository proficiencyLevelRepository,
                                 EvaluationOverallRepository evaluationOverallRepository,
                                 SkillSetRepository skillSetRepository,
                                 DepartmentRepository departmentRepository,
                                 EmployeeManagementService employeeManagementService,
                                 JobLevelRepository jobLevelRepository,
                                 PositionJobLevelSkillSetRepository positionLevelSkillSetRepository,
                                 PositionDepartmentRepository positionDepartmentRepository, EmployeeDamInfoRepository employeeDamInfoRepository,
                                 CareerSpecification careerSpecification,
                                 EmployeeSpecification employeeSpecification,
                                 CompetencySpecification competencySpecification,
                                 PerformanceCycleRepository performanceCycleRepository,
                                 PerformanceEvaluationRepository performanceEvaluationRepository,
                                 PerformanceSpecification performanceSpecification,
                                 CareerManagementService careerManagementService) {
        this.competencyEvaluationRepository = competencyEvaluationRepository;
        this.employeeRepository = employeeRepository;
        this.competencyTimeLineRepository = competencyTimeLineRepository;
        this.skillSetEvaluationRepository = skillSetEvaluationRepository;
        this.skillSetTargetRepository = skillSetTargetRepository;
        this.positionSkillSetRepository = positionSkillSetRepository;
        this.competencyRepository = competencyRepository;
        this.competencyCycleRepository = competencyCycleRepository;
        this.proficiencyLevelRepository = proficiencyLevelRepository;
        this.evaluationOverallRepository = evaluationOverallRepository;
        this.skillSetRepository = skillSetRepository;
        this.departmentRepository = departmentRepository;
        this.employeeManagementService = employeeManagementService;
        this.jobLevelRepository = jobLevelRepository;
        this.positionLevelSkillSetRepository = positionLevelSkillSetRepository;
        this.positionDepartmentRepository = positionDepartmentRepository;
        this.employeeDamInfoRepository = employeeDamInfoRepository;
        this.careerSpecification = careerSpecification;
        this.employeeSpecification = employeeSpecification;
        this.competencySpecification = competencySpecification;
        this.performanceCycleRepository = performanceCycleRepository;
        this.performanceEvaluationRepository = performanceEvaluationRepository;
        this.performanceSpecification = performanceSpecification;
        this.careerManagementService = careerManagementService;
    }

    @PostConstruct
    private void initialize() {
        this.latestCompCycle = getLatestCompCycle();
        this.latestPerformCycle = getLatestPerformCycle();
    }

    private PerformanceCycle getLatestPerformCycle() {
        return performanceCycleRepository.findFirstByOrderByPerformanceCycleStartDateDesc();
    }

    private CompetencyCycle getLatestCompCycle() {
        return competencyCycleRepository.findFirstByOrderByStartDateDesc();
    }

    public List<SkillSetEvaluation> getSkillEvaluations(Integer employeeId, Integer cycleId) {
        Specification<SkillSetEvaluation> empSpec = employeeSpecification.hasEmployeeId(employeeId);
        Specification<SkillSetEvaluation> cycSpec = GlobalSpec.hasCompCycleId(cycleId);
        return skillSetEvaluationRepository.findAll(empSpec.and(cycSpec));
    }

    public List<SkillSet> getBaselineSkillsSet(Integer positionId, Integer levelId) {
        Specification<PositionJobLevelSkillSet> posSpec = careerSpecification.hasPositionId(positionId);
        Specification<PositionJobLevelSkillSet> levelSpec = careerSpecification.hasJobLevelId(levelId);
        return positionLevelSkillSetRepository.findAll(posSpec.and(levelSpec))
                .stream()
                .map(PositionJobLevelSkillSet::getSkillSet)
                .toList();  //Have not optimized yet
    }

    public List<SkillSet> getTargetSkillsSet(Integer employeeId, Integer cycleId) {
        Specification<SkillSetTarget> empSpec = employeeSpecification.hasEmployeeId(employeeId);
        Specification<SkillSetTarget> cycSpec = competencySpecification.hasCycleId(cycleId);

        return skillSetTargetRepository.findAll(empSpec.and(cycSpec))
                .stream().map(SkillSetTarget::getSkillSet).toList();
    }

    public List<CompetencyEvaluation> getCompetencyEvaluations(Integer employeeId, Integer cycleId) {
        Specification<CompetencyEvaluation> empSpec = employeeSpecification.hasEmployeeId(employeeId);
        Specification<CompetencyEvaluation> cycleSpec = competencySpecification.hasCycleId(cycleId);
        return competencyEvaluationRepository.findAll(empSpec.and(cycleSpec));
    }

    /**
     * if skillEval is null, currentScore will be null
     * if targetSkill is null, targetScore will be null
     *
     * @return SkillSummarization (DTO)
     */
    public BarChartDTO getSkillSetGap(Integer employeeId, Integer cycleId) {
        //1. Skill Set Average Score
        var skillSetAvgScore = getAverageSkillSet(employeeId, cycleId).orElse(null);

        //2. Skill Set Target Score
        var position = getPosition(employeeId);
        var level = getLevel(employeeId);
        var skillSetBaselineScore = careerManagementService.getBaselineSkillSetAvgScore(position.getId(), level.getId())
                .orElse(null);

        DataItemDTO current = new DataItemDTO("Current", skillSetAvgScore);
        DataItemDTO target = new DataItemDTO("Target", skillSetBaselineScore);
        return new BarChartDTO("Skill Gap Chart", List.of(current, target));
    }

    public PieChartDTO getCompetencyLevelPieChart(Integer employeeId, Integer cycleId) {
        var currentScore = getAverageSkillSet(employeeId, cycleId);
        var targetScore = careerManagementService.getBaselineSkillSetAvgScore(getPosition(employeeId).getId(),
                getLevel(employeeId).getId());

        return new PieChartDTO(List.of("Current Score", "Target Score"),
                List.of(currentScore.orElse(null).floatValue(), targetScore.orElse(null).floatValue()));
    }

    @Override
    public MultiBarChartDTO getSumDepartmentIncompletePercent(Integer cycleId, Integer departmentId) {
        Specification<CompetencyEvaluationOverall> hasCompCycle = GlobalSpec.hasCompCycleId(cycleId);
        Specification<CompetencyEvaluationOverall> hasEmployeeDepartment = GlobalSpec.hasEmployeeDepartmentId(departmentId);

        var evaluations = evaluationOverallRepository.findAll(hasCompCycle.and(hasEmployeeDepartment));

        // Position in the department
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Position> query = cb.createQuery(Position.class);
        Root<PositionDepartment> root = query.from(PositionDepartment.class);

        query.select(root.get("position"))
                .where(cb.equal(root.get("department").get("id"), departmentId));

        var result = new MultiBarChartDTO(List.of("Self", "Manager"), List.of());
        var positions = entityManager.createQuery(query).getResultList();

        for (var pos : positions) {
            var completedCount = evaluations.stream()
                    .filter(evaluation -> evaluation.getEmployee().getPosition().getId() == pos.getId())
                    .filter(evaluation -> evaluation.getFinalStatus().equals(COMPETENCY_COMPLETED_STATUS))
                    .count();

            var incompleteCount = evaluations.stream()
                    .filter(evaluation -> evaluation.getEmployee().getPosition().getId() == pos.getId())
                    .filter(evaluation -> !evaluation.getFinalStatus().equals(COMPETENCY_COMPLETED_STATUS))
                    .count();

            var data = List.of((float) incompleteCount, (float) completedCount);

            result.getDatasets().add(new MultiBarChartDataDTO(pos.getPositionName(), data));
        }

        return result;
    }

    @Override
    public EmployeeStatusPagination getCompetencyEvaluationsStatus(Integer cycleId, Integer departmentId, Pageable page) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<NameAndStatusOnly> query = cb.createQuery(NameAndStatusOnly.class);
        Root<CompetencyEvaluationOverall> root = query.from(CompetencyEvaluationOverall.class);
        Join<CompetencyEvaluationOverall, Employee> empJoin = root.join("employee");

        query.multiselect(root.get("employee").get("id"), root.get("employee").get("firstName"), root.get("employee").get("lastName"), root.get("finalStatus"))
                .where(cb.equal(root.get("competencyCycle").get("id"), cycleId),
                        cb.equal(empJoin.get("department").get("id"), departmentId));

        var nameAndStatusList = entityManager.createQuery(query)
                .setFirstResult((int) page.getOffset())
                .setMaxResults(page.getPageSize())
                .getResultList();

        var empIdsSet = nameAndStatusList.stream().map(NameAndStatusOnly::id).toList();

        var profileImages = employeeDamInfoRepository.findByEmployeeIdsSetAndFileType(empIdsSet, PROFILE_IMAGE);

        var result = nameAndStatusList.stream()
                .map(item -> new EmployeeStatusDTO(item.id(), item.firstName(), item.lastName(), item.status(),
                        profileImages.stream()
                                .filter(profile -> profile.getEmployeeId().equals(item.id()))
                                .map(ProfileImageOnly::getUrl)
                                .findFirst()
                                .orElse(null)))
                .toList();

        var total = entityManager.createQuery(query).getResultList().size();
        var pagination = setupPaging(total, page.getPageNumber() + 1, page.getPageSize());

        return new EmployeeStatusPagination(result, pagination);
    }

    public Optional<Double> getAverageSkillSet(Integer empId, Integer cycleId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Double> query = cb.createQuery(Double.class);
        Root<SkillSetEvaluation> root = query.from(SkillSetEvaluation.class);
        Join<SkillSetEvaluation, ProficiencyLevel> proficencyJoin = root.join("employeeProficiencyLevel");

        query.select(cb.avg(proficencyJoin.get("score")));
        query.where(cb.equal(root.get("employee").get("id"), empId),
                cb.equal(root.get("competencyCycle").get("id"), cycleId));

        return Optional.ofNullable(entityManager.createQuery(query).getSingleResult());
    }

    private Position getPosition(Integer employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee not found"))
                .getPosition();
    }

    private JobLevel getLevel(Integer employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee not found"))
                .getJobLevel();
    }


    @Override
    @Scheduled(cron = "0 0 0 * * *")
    public void updateIsDoneForOverdueItems() {
        competencyTimeLineRepository.updateIsDoneForOverdueItems();
    }

    @Override
    public List<TimeLine> getCompetencyTimeline(Integer competencyCycleId) {
        Specification<CompetencyTimeLine> spec = GlobalSpec.hasCompCycleId(competencyCycleId);
        return competencyTimeLineRepository.findAll(spec)
                .stream()
                .map(item -> new TimeLine(
                        item.getCompetencyTimeLineName(),
                        item.getStartDate().toString(), item.getDueDate().toString(),
                        item.getIsDone()))
                .toList();
    }

    @Override
    public MultiBarChartDTO getDepartmentInCompleteComp(Integer competencyCycleId) {
        List<Department> departments = departmentRepository.findAll();
        List<Employee> employees = employeeManagementService.getAllEmployeesHaveDepartment();

        //Get all CompetencyEvaluationOverall of all employees have final status is agreed and get the latest cycle


        List<Float> selfData = processDepartmentData(departments, SELF_EVAL_LABEL_NAME, competencyCycleId, employees);
        List<Float> managerData = processDepartmentData(departments, SUPERVISOR_EVAL_LABEL_NAME, competencyCycleId, employees);

        List<MultiBarChartDataDTO> datasets = new ArrayList<>();
        datasets.add(new MultiBarChartDataDTO(SELF_EVAL_LABEL_NAME, selfData));
        datasets.add(new MultiBarChartDataDTO(SUPERVISOR_EVAL_LABEL_NAME, managerData));

        List<String> labels = departments.stream().map(Department::getDepartmentName).toList();
        return new MultiBarChartDTO(labels, datasets);
    }

    private List<Float> processDepartmentData(List<Department> departments, String type,
                                              Integer competencyCycleId, List<Employee> employees) {
        return departments.parallelStream().map(item -> {
            List<Integer> departmentEmpIds = employees.stream()
                    .filter(emp -> emp.getDepartment().getId() == item.getId())
                    .map(Employee::getId)
                    .toList();

            return (type.equals(SELF_EVAL_LABEL_NAME))
                    ? getEmployeeInCompletedPercent(competencyCycleId, departmentEmpIds)
                    : getEvaluatorInCompletePercent(competencyCycleId, departmentEmpIds);
        }).toList();
    }

    private Float getEvaluatorInCompletePercent(Integer competencyCycleId, List<Integer> empIdSet) {
        return calculatePercentage(competencyCycleId, empIdSet, "evaluatorStatus");
    }

    private Float getEmployeeInCompletedPercent(Integer competencyCycleId, List<Integer> empIdSet) {
        return calculatePercentage(competencyCycleId, empIdSet, "employeeStatus");
    }

    private Float calculatePercentage(Integer competencyCycleId, List<Integer> empIdSet, String evalType) {
        if (empIdSet.isEmpty()) return null;
        Specification<CompetencyEvaluationOverall> hasTypeCompleted = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(evalType), "Completed");
        Specification<CompetencyEvaluationOverall> hasEmployeeIds = GlobalSpec.hasEmployeeIds(empIdSet);
        Specification<CompetencyEvaluationOverall> hasCycleId = GlobalSpec.hasCompCycleId(competencyCycleId);

        var completedCount = evaluationOverallRepository.count(hasTypeCompleted.and(hasEmployeeIds).and(hasCycleId));
        return (float) (empIdSet.size() - completedCount) / empIdSet.size() * 100;
    }

    @Override
    public PieChartDTO getCompetencyEvalProgress(Integer competencyCycleId) {

        List<Integer> empIdSet = employeeManagementService.getAllEmployees()
                .stream()
                .map(Employee::getId)
                .toList();

        //get all employees who have completed evaluation
        Specification<CompetencyEvaluationOverall> hasStatusComplete = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("finalStatus"), "Completed");
        Specification<CompetencyEvaluationOverall> hasEmployeeIds = GlobalSpec.hasEmployeeIds(empIdSet);
        Specification<CompetencyEvaluationOverall> hasCycleId = GlobalSpec.hasCompCycleId(competencyCycleId);

        List<Float> datasets = new ArrayList<>();
        var completedPercent = (float) evaluationOverallRepository
                .count(hasStatusComplete.and(hasEmployeeIds).and(hasCycleId)) / empIdSet.size() * 100;
        datasets.add(completedPercent);
        datasets.add(100 - completedPercent);

        return new PieChartDTO(List.of(COMPLETED_LABEL_NAME, IN_COMPLETED_LABEL_NAME), datasets);
    }


    @Override
    public List<HeatmapItemDTO> getHeatmapCompetency(Integer positionId, Integer competencyCycleId) {
        List<CompetencyEvaluation> compEvaluates = fetchCompetencyEvaluations(positionId, competencyCycleId);
        List<JobLevel> jobLevels = jobLevelRepository.findAll();
        List<Competency> competencies = competencyRepository.findAll();

        return jobLevels.stream()
                .flatMap(jobLevel -> competencies.stream()
                        .map(competency -> new Pair<>(jobLevel, competency)))
                .map(pair -> calculateAvgCompetency(compEvaluates, pair))
                .toList();
    }

    private List<CompetencyEvaluation> fetchCompetencyEvaluations(Integer positionId, Integer competencyCycleId) {
        Specification<CompetencyEvaluation> hasCycSpec = competencySpecification.hasCycleId(competencyCycleId);
        Specification<CompetencyEvaluation> hasPosSpec = employeeSpecification.hasPositionId(positionId);

        return positionId != null
                ? competencyEvaluationRepository.findAll(hasCycSpec.and(hasPosSpec))
                : competencyEvaluationRepository.findAll(hasCycSpec);
    }

    private HeatmapItemDTO calculateAvgCompetency(List<CompetencyEvaluation> compEvaluates,
                                                  Pair<JobLevel, Competency> pair) {
        JobLevel jobLevel = pair.getFirst();
        Competency competency = pair.getSecond();

        List<CompetencyEvaluation> evaluationsHasJobLevelAndCompetency = compEvaluates.stream()
                .filter(compEva -> compEva.getEmployee().getJobLevel().getId() == jobLevel.getId()
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


    @Override
    public DataItemPagingDTO getTopSkillSet(@Nullable Integer departmentId, @Nullable Integer employeeId,
                                            Integer competencyCycleId, int pageNo, int pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DataItemDTO> criteriaQuery = criteriaBuilder.createQuery(DataItemDTO.class);

        Root<SkillSetEvaluation> skillSetEvaluationRoot = criteriaQuery.from(SkillSetEvaluation.class);
        Join<SkillSetEvaluation, ProficiencyLevel> proficiencyLevelJoin = skillSetEvaluationRoot.join("finalProficiencyLevel");
        Join<SkillSetEvaluation, Employee> employeeJoin = skillSetEvaluationRoot.join("employee");
        Join<SkillSetEvaluation, SkillSet> skillSetJoin = skillSetEvaluationRoot.join("skillSet");

        criteriaQuery.multiselect(
                skillSetJoin.get("skillSetName").alias("label"),
                proficiencyLevelJoin.get("score").alias("value")
        );

        List<Integer> employeeIds = new ArrayList<>();
        if(employeeId != null) employeeIds.add(employeeId);
        if(departmentId != null) {
            employeeIds.addAll(employeeManagementService.getEmployeesInDepartment(departmentId)
                    .stream()
                    .map(Employee::getId)
                    .toList());
        }

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(skillSetEvaluationRoot.get("competencyCycle").get("id"), competencyCycleId));

        if (!employeeIds.isEmpty()) {
            predicates.add(employeeJoin.get("id").in(employeeIds));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        criteriaQuery.orderBy(criteriaBuilder.desc(proficiencyLevelJoin.get("score")));

        List<DataItemDTO> dataItemDTOS = entityManager.createQuery(criteriaQuery).getResultList();

        Pagination pagination = setupPaging(dataItemDTOS.size(), pageNo, pageSize);
        return new DataItemPagingDTO(dataItemDTOS, pagination);
    }

    @Override
    public DataItemPagingDTO getTopKeenSkillSetEmployee(Integer employeeId, int pageNo, int pageSize) {
        CompetencyCycle evalLatestCycle = evaluationOverallRepository.latestEvalCompetencyCycle(employeeId);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DataItemDTO> criteriaQuery = criteriaBuilder.createQuery(DataItemDTO.class);

        Root<SkillSetEvaluation> sseRoot = criteriaQuery.from(SkillSetEvaluation.class);
        Join<SkillSetEvaluation, ProficiencyLevel> plJoin = sseRoot.join("finalProficiencyLevel");
        Join<SkillSetEvaluation, SkillSet> ssJoin = sseRoot.join("skillSet");

        criteriaQuery.multiselect(
                ssJoin.get("skillSetName").alias("label"),
                plJoin.get("score").alias("value"));

        criteriaQuery.where(
                criteriaBuilder.and(
                        criteriaBuilder.equal(sseRoot.get("competencyCycle").get("id"), evalLatestCycle.getId()),
                        criteriaBuilder.equal(sseRoot.get("employee").get("id"), employeeId)
                )
        );

        criteriaQuery.orderBy(criteriaBuilder.asc(plJoin.get("score")));

        TypedQuery<DataItemDTO> query = entityManager.createQuery(criteriaQuery);
        List<DataItemDTO> results = query.getResultList();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<DataItemDTO> topsHighest = new PageImpl<>(results, pageable, results.size());
        Pagination pagination = setupPaging(topsHighest.getTotalElements(), pageNo, pageSize);
        return new DataItemPagingDTO(topsHighest.getContent(), pagination);
    }

    @Override
    public DataItemPagingDTO getTopSkillSetTargetEmployee(Integer employeeId, int pageNo, int pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DataItemDTO> criteriaQuery = criteriaBuilder.createQuery(DataItemDTO.class);

        Root<SkillSetTarget> sseRoot = criteriaQuery.from(SkillSetTarget.class);
        Join<SkillSetTarget, ProficiencyLevel> plJoin = sseRoot.join("targetProficiencyLevel");
        Join<SkillSetTarget, SkillSet> ssJoin = sseRoot.join("skillSet");

        criteriaQuery.multiselect(
                ssJoin.get("skillSetName").alias("label"),
                plJoin.get("score").alias("value"));

        criteriaQuery.where(
                criteriaBuilder.and(
                        criteriaBuilder.equal(sseRoot.get("competencyCycle").get("id"), latestCompCycle.getId()),
                        criteriaBuilder.equal(sseRoot.get("employee").get("id"), employeeId)
                )
        );

        criteriaQuery.orderBy(criteriaBuilder.asc(plJoin.get("score")));

        TypedQuery<DataItemDTO> query = entityManager.createQuery(criteriaQuery);
        List<DataItemDTO> results = query.getResultList();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<DataItemDTO> topsHighest = new PageImpl<>(results, pageable, results.size());
        Pagination pagination = setupPaging(topsHighest.getTotalElements(), pageNo, pageSize);
        return new DataItemPagingDTO(topsHighest.getContent(), pagination);
    }

    @Override
    public List<CurrentEvaluationDTO> getCurrentEvaluation(Integer employeeId) {
        CurrentEvaluationDTO compEval = getCurrentCompEval(employeeId);

        CurrentEvaluationDTO perfEvalDTO = getCurrentPerformEval(employeeId);

        return List.of(compEval, perfEvalDTO);
    }

    @NotNull
    private CurrentEvaluationDTO getCurrentPerformEval(Integer employeeId) {
        Specification<PerformanceEvaluation> hasEmpId = performanceSpecification.hasEmployeeId(employeeId);
        Specification<PerformanceEvaluation> hasCycleIds = performanceSpecification.hasCycleId(latestPerformCycle.getPerformanceCycleId());
        PerformanceEvaluation perfEval = performanceEvaluationRepository
                .findOne(hasEmpId.and(hasCycleIds))
                .orElse(null);
        return perfEval == null
                ? new CurrentEvaluationDTO(latestPerformCycle.getPerformanceCycleName(), "Not Started", null)
                : new CurrentEvaluationDTO(perfEval.getPerformanceCycle().getPerformanceCycleName(),
                perfEval.getStatus(), perfEval.getLastUpdated().toString());
    }

    @NotNull
    private CurrentEvaluationDTO getCurrentCompEval(Integer employeeId) {
        Specification<CompetencyEvaluationOverall> hasEmpId = employeeSpecification.hasEmployeeId(employeeId);
        Specification<CompetencyEvaluationOverall> hasCompCycleIds = competencySpecification.hasCycleId(latestCompCycle.getId());
        CompetencyEvaluationOverall evalOvr = evaluationOverallRepository
                .findOne(hasEmpId.and(hasCompCycleIds))
                .orElse(null);
        return evalOvr == null
                ? new CurrentEvaluationDTO(latestCompCycle.getCompetencyCycleName(), "Not Started", null)
                : new CurrentEvaluationDTO(evalOvr.getCompetencyCycle().getCompetencyCycleName(),
                evalOvr.getFinalStatus(), evalOvr.getLastUpdated().toString());
    }

    @Override
    public List<HistoryEvaluationDTO> getHistoryEvaluations(Integer employeeId) {
        List<HistoryEvaluationDTO> compEvaluates = getCompetencyEvalHistory(employeeId);

        List<HistoryEvaluationDTO> performEvaluates = getPerformEvalHistory(employeeId);

        return Stream.of(compEvaluates, performEvaluates)
                .flatMap(Collection::stream)
                .toList();
    }

    @NotNull
    private List<HistoryEvaluationDTO> getPerformEvalHistory(Integer employeeId) {
        List<Integer> performCycleIds = performanceCycleRepository.findAll()
                .stream()
                .map(PerformanceCycle::getPerformanceCycleId)
                .toList();

        Specification<PerformanceEvaluation> hasEmployeeId = performanceSpecification.hasEmployeeId(employeeId);
        Specification<PerformanceEvaluation> hasPerformanceCycleIds = performanceSpecification.hasPerformanceCycleIds(performCycleIds);

        return performanceEvaluationRepository
                .findAll(hasEmployeeId.and(hasPerformanceCycleIds))
                .stream()
                .map(eval -> new HistoryEvaluationDTO(eval.getCompletedDate().toString(),
                        eval.getPerformanceCycle().getPerformanceCycleName(),
                        eval.getStatus(), eval.getFinalAssessment()))
                .toList();
    }

    @NotNull
    private List<HistoryEvaluationDTO> getCompetencyEvalHistory(Integer employeeId) {
        List<Integer> compCycleIds = competencyCycleRepository.findAll()
                .stream()
                .map(CompetencyCycle::getId)
                .toList();
        Specification<CompetencyEvaluationOverall> hasEmployeeId = employeeSpecification.hasEmployeeId(employeeId);
        Specification<CompetencyEvaluationOverall> hasCompetencyCycleIds = competencySpecification.hasCycleIds(compCycleIds);

        return evaluationOverallRepository
                .findAll(hasEmployeeId.and(hasCompetencyCycleIds))
                .stream()
                .map(evalOvr -> new HistoryEvaluationDTO(evalOvr.getCompletedDate().toString(),
                        evalOvr.getCompetencyCycle().getCompetencyCycleName(),
                        evalOvr.getFinalStatus(), evalOvr.getScore()))
                .toList();
    }


    @Override
    public DiffPercentDTO getCompanyCompetencyDiffPercent() {
        //Find competencyCycle has the latest due date and status is Completed
        Specification<CompetencyCycle> cycleSpec = (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("status"), "Completed")
        );
        CompetencyCycle currentCycle = competencyCycleRepository.findAll(cycleSpec)
                .stream()
                .max(Comparator.comparing(CompetencyCycle::getDueDate))
                .orElseThrow(() -> new RuntimeException("No cycle found"));

        float avgCurrentEvalScore = getAvgEvalScore(currentCycle.getId());

        //Get previous cycle by current year - 1
        Integer previousYear = currentCycle.getYear() - 1;
        Integer previousCycleId = competencyCycleRepository.findByYear(previousYear).getId();
        float avgPreviousEvalScore = getAvgEvalScore(previousCycleId);

        float diffPercentage = ((avgCurrentEvalScore - avgPreviousEvalScore) / avgPreviousEvalScore) * 100;

        //Get the highest score of proficiency level
        float highestScore = proficiencyLevelRepository.findFirstByOrderByScoreDesc().getScore();
        //String format avgPreviousEvalScore/highestScore
        String data = String.format("%.1f/%.1f", avgPreviousEvalScore, highestScore);

        return new DiffPercentDTO(data, diffPercentage, diffPercentage > 0);
    }

    private float getAvgEvalScore(Integer cycleId) {
        //Get all evaluation overall of all employees have final status is agreed and get the latest cycle
        Specification<CompetencyEvaluationOverall> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("finalStatus"), "Completed");
        Specification<CompetencyEvaluationOverall> hasCompCycle = GlobalSpec.hasCompCycleId(cycleId);
        List<Float> evalScores = evaluationOverallRepository.findAll(spec.and(hasCompCycle))
                .stream()
                .map(CompetencyEvaluationOverall::getScore)
                .toList();

        return (float) evalScores.stream().mapToDouble(Float::doubleValue).average().orElse(0);
    }

    @Override
    public BarChartDTO getCompetencyChart() {
        int currentYear = latestCompCycle.getDueDate().before(Calendar.getInstance().getTime())
                ? latestCompCycle.getYear()
                : latestCompCycle.getYear() - 1;
        Integer currentCycleId = competencyCycleRepository.findByYear(currentYear).getId();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DataItemDTO> criteriaQuery = criteriaBuilder.createQuery(DataItemDTO.class);

        Root<CompetencyEvaluation> plRoot = criteriaQuery.from(CompetencyEvaluation.class);
        Join<CompetencyEvaluation, Competency> cJoin = plRoot.join("competency");

        criteriaQuery.multiselect(
                cJoin.get("competencyName").alias("label"),
                criteriaBuilder.avg(plRoot.get("finalEvaluation")).alias("value")
        );

        criteriaQuery.where(
                criteriaBuilder.equal(plRoot.get("competencyCycle").get("id"), currentCycleId)
        );

        criteriaQuery.groupBy(plRoot.get("competency").get("id"));

        TypedQuery<DataItemDTO> query = entityManager.createQuery(criteriaQuery);
        List<DataItemDTO> results = query.getResultList();

        return new BarChartDTO("Competency", results);
    }

    @Override
    public RadarChartDTO getOverallCompetencyRadarChart(Integer employeeId, Integer cycleId) throws RuntimeException {
        Specification<CompetencyEvaluation> hasEmpId = employeeSpecification.hasEmployeeId(employeeId);
        Specification<CompetencyEvaluation> hasCycId = competencySpecification.hasCycleId(cycleId);

        var evaluations = competencyEvaluationRepository.findAll(hasEmpId.and(hasCycId));
        var competencies = competencyRepository.findAll()
                .stream().sorted(Comparator.comparing(Competency::getOrdered)).toList();

        var selfEvalData = new RadarDatasetDTO(SELF_EVAL_LABEL_NAME, new ArrayList<>());
        var supervisorEvalData = new RadarDatasetDTO(SUPERVISOR_EVAL_LABEL_NAME, new ArrayList<>());
        var finalEvalData = new RadarDatasetDTO(FINAL_EVAL_LABEL_NAME, new ArrayList<>());

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

    private List<SkillSetTarget> getSkillSetTargets(Integer employeeId, Integer latestCompId) {
        Specification<SkillSetTarget> hasEmployeeId = GlobalSpec.hasEmployeeId(employeeId);
        Specification<SkillSetTarget> hasCompCycleId = GlobalSpec.hasCompCycleId(latestCompId);
        return skillSetTargetRepository.findAll(hasEmployeeId.and(hasCompCycleId));
    }

    private List<PositionSkillSet> getPositionSkillSets(Integer positionId, List<Integer> competencyId) {
        //Find PositionSkillSet has positionId = positionId and competencyId in competencyIds
        Specification<PositionSkillSet> posSpec = (root, query, builder) ->
                root.get("skillSet").get("competency").get("id").in(competencyId);
        Specification<PositionSkillSet> hasPositionId = GlobalSpec.hasPositionId(positionId);
        return positionSkillSetRepository.findAll(posSpec.and(hasPositionId));
    }

    private List<SkillSetEvaluation> getSkillSetEvaluations(Integer employeeId, Integer cycleId) {
        Specification<SkillSetEvaluation> hasEmployeeId = GlobalSpec.hasEmployeeId(employeeId);
        Specification<SkillSetEvaluation> hasCycleId = GlobalSpec.hasCompCycleId(cycleId);
        return skillSetEvaluationRepository.findAll(hasEmployeeId.and(hasCycleId));
    }


    @Override
    public List<EmployeeSkillMatrixDTO> getEmployeeSkillMatrix(Integer empId) {
        Employee employee = employeeManagementService.findEmployee(empId);
        skillSetRepository.findAll();
        proficiencyLevelRepository.findAll();
        Integer latestCompEvaId = evaluationOverallRepository.latestEvalCompetencyCycle(empId).getId();
        Integer latestCompId = latestCompCycle.getId();


        List<Competency> competencies = competencyRepository.findAll();

        //Setup data:
        List<Integer> competencyIds = competencies.stream().map(Competency::getId).toList();
        List<PositionSkillSet> listPoSs = getPositionSkillSets(employee.getPosition().getId(), competencyIds);
        List<SkillSetEvaluation> ssEvaluates = getSkillSetEvaluations(empId, latestCompEvaId);
        List<SkillSetTarget> ssTargets = getSkillSetTargets(employee.getId(), latestCompId);

        return competencies.stream()
                .map(competency -> {
                    List<EmployeeSkillMatrixDTO> children =
                            handleChildren(competency.getId(), listPoSs, ssEvaluates, ssTargets);
                    SkillMatrixDataDTO smData = calculateSkillMatrixData(competency.getCompetencyName(), children);
                    return new EmployeeSkillMatrixDTO(smData, children);
                })
                .toList();
    }

    private List<EmployeeSkillMatrixDTO> handleChildren(Integer competencyId,
                                                        List<PositionSkillSet> listPoSs,
                                                        List<SkillSetEvaluation> ssEvaluates,
                                                        List<SkillSetTarget> ssTargets) {
        List<PositionSkillSet> listPoSsFilter = listPoSs
                .stream()
                .filter(item -> Objects.equals(item.getSkillSet().getCompetency().getId(), competencyId))
                .toList();

        List<Integer> listSkillSetIds = listPoSsFilter.stream().map(item -> item.getSkillSet().getId()).toList();
        List<SkillSetEvaluation> ssEvaluatesFilter = ssEvaluates
                .stream()
                .filter(item -> listSkillSetIds.contains(item.getSkillSet().getId()))
                .toList();
        List<SkillSetTarget> ssTargetsFilter = ssTargets
                .stream()
                .filter(item -> listSkillSetIds.contains(item.getSkillSet().getId()))
                .toList();

        return listPoSsFilter.stream()
                .map(item -> {
                    SkillMatrixDataDTO smDataChild = calculateSkillMatrixDataChild(item, ssEvaluatesFilter, ssTargetsFilter);
                    return new EmployeeSkillMatrixDTO(smDataChild, null);
                })
                .toList();
    }

    private SkillMatrixDataDTO calculateSkillMatrixDataChild(PositionSkillSet item,
                                                             List<SkillSetEvaluation> ssEvaluates,
                                                             List<SkillSetTarget> ssTargets) {
        SkillSetEvaluation ssEva = ssEvaluates.stream()
                .filter(ssEvaluate -> ssEvaluate.getSkillSet().getId() == item.getSkillSet().getId())
                .findFirst()
                .orElse(null);

        SkillSetTarget ssTarget = ssTargets.stream()
                .filter(ssT -> ssT.getSkillSet().getId() == item.getSkillSet().getId())
                .findFirst()
                .orElse(null);

        return ssEva != null && ssTarget != null ? new SkillMatrixDataDTO(
                item.getSkillSet().getSkillSetName(),
                (double) ssTarget.getTargetProficiencyLevel().getScore(),
                (double) ssEva.getFinalProficiencyLevel().getScore(),
                (double) ssEva.getEmployeeProficiencyLevel().getScore(),
                (double) ssEva.getEvaluatorProficiencyLevel().getScore(),
                ((double) ssEva.getFinalProficiencyLevel().getScore() / (double) ssTarget.getTargetProficiencyLevel().getScore()) * 100)
                : null;
    }

    private SkillMatrixDataDTO calculateSkillMatrixData(String competencyName, List<EmployeeSkillMatrixDTO> children) {
        int totalSkillSet = children.size();

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
                targetScore / totalSkillSet,
                totalScore / totalSkillSet,
                selfScore / totalSkillSet,
                evaluatorScore / totalSkillSet,
                competencyScore / totalSkillSet
        );
    }


    @Override
    public SkillMatrixOverallDTO getSkillMatrixOverall(Integer empId) {
        CompetencyCycle latestCompEva = evaluationOverallRepository.latestEvalCompetencyCycle(empId);
        Specification<CompetencyEvaluationOverall> hasEmployeeId = GlobalSpec.hasEmployeeId(empId);
        Specification<CompetencyEvaluationOverall> hasCompCycleId = GlobalSpec.hasCompCycleId(latestCompEva.getId());

        CompetencyEvaluationOverall eval = evaluationOverallRepository.findOne(hasEmployeeId.and(hasCompCycleId)).orElse(null);
        Employee employee = employeeManagementService.findEmployee(empId);
        return SkillMatrixOverallDTO.builder()
                .managerName(employee.getDepartment().getSum().getFullName())
                .status(Objects.requireNonNull(eval).getFinalStatus()).build();
    }

    @Override
    public RadarChartDTO getCompetencyRadarChart(List<Integer> competencyCyclesId, Integer departmentId) {
        List<Competency> competencies = competencyRepository.findAll();
        List<CompetencyCycle> competencyCycles = competencyCycleRepository.findAll();
        List<CompetencyEvaluation> competencyEvaluates = findByCyclesAndDepartment(competencyCyclesId, departmentId);
        proficiencyLevelRepository.findAll();

        List<Pair<Integer, Integer>> pairItems = createPairItems(competencyCyclesId, competencies);

        List<RadarValueDTO> avgCompetencies = calculateAverageCompetencies(pairItems, competencyEvaluates);

        List<RadarDatasetDTO> listDataset = createRadarDataset(competencyCyclesId, competencies, avgCompetencies, competencyCycles);

        List<String> labels = competencies.stream().map(Competency::getCompetencyName).toList();
        return new RadarChartDTO(labels, listDataset);
    }

    private List<CompetencyEvaluation> findByCyclesAndDepartment(List<Integer> competencyCyclesId, Integer departmentId) {
        Specification<CompetencyEvaluation> hasCompetencyCycles = GlobalSpec.hasCompCycleIds(competencyCyclesId);
        Specification<CompetencyEvaluation> hasEmployeeDepartment = GlobalSpec.hasEmployeeDepartmentId(departmentId);
        return competencyEvaluationRepository.findAll(hasCompetencyCycles.and(hasEmployeeDepartment));
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
                    .filter(compEva -> compEva.getCompetencyCycle().getId() == cycleId
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

    private List<RadarDatasetDTO> createRadarDataset(List<Integer> competencyCyclesId,
                                                     List<Competency> competencies,
                                                     List<RadarValueDTO> avgCompetencies,
                                                     List<CompetencyCycle> competencyCycles) {
        return competencyCyclesId.stream().map(cycle -> {
            List<Float> listScore = competencies.stream()
                    .map(competency -> avgCompetencies.stream()
                            .filter(avgCompetency -> avgCompetency.getCompetencyId().equals(competency.getId()) &&
                                    avgCompetency.getInputId().equals(cycle))
                            .findFirst()
                            .map(RadarValueDTO::getAverage)
                            .orElse(null))
                    .toList();
            return new RadarDatasetDTO(
                    competencyCycles.stream()
                            .filter(competencyCycle -> competencyCycle.getId() == cycle)
                            .findFirst()
                            .orElseThrow()
                            .getCompetencyCycleName(),
                    listScore);
        }).toList();
    }


    private Float getAvgSkillSetScore(Integer employeeId, Integer cycleId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Float> query = criteriaBuilder.createQuery(Float.class);
        Root<SkillSetEvaluation> root = query.from(SkillSetEvaluation.class);
        Join<SkillSetEvaluation, ProficiencyLevel> proficencyJoin = root.join("finalProficiencyLevel");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(entityManager.getCriteriaBuilder().equal(root.get("employee").get("id"), employeeId));
        predicates.add(entityManager.getCriteriaBuilder().equal(root.get("competencyCycle").get("id"), cycleId));

        query.multiselect(criteriaBuilder.avg(proficencyJoin.get("score"))).where(predicates.toArray(new Predicate[]{}));

        return entityManager.createQuery(query).getSingleResult();
    }

    private Float getAvgBaselineSkillSet(Integer positionId, Integer jobLevelId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Float> query = criteriaBuilder.createQuery(Float.class);
        Root<PositionJobLevelSkillSet> root = query.from(PositionJobLevelSkillSet.class);
        Join<PositionJobLevelSkillSet, ProficiencyLevel> proficencyJoin = root.join("proficiencyLevel");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(entityManager.getCriteriaBuilder().equal(root.get("position").get("id"), positionId));
        predicates.add(entityManager.getCriteriaBuilder().equal(root.get("jobLevel").get("id"), jobLevelId));

        query.multiselect(criteriaBuilder.avg(proficencyJoin.get("score"))).where(predicates.toArray(new Predicate[]{}));

        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public List<CompetencyCycle> getCompetencyCycles() {
        return competencyCycleRepository.findAll();
    }

    @Override
    public EmployeeRatingPagination getCompetencyRating(Integer departmentId, Integer cycleId,
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
                ceoRoot.get("score").alias("rating")
        );

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(ceoRoot.get("competencyCycle").get("id"), cycleId));

        if (departmentId != null) {
            predicates.add(criteriaBuilder.equal(employeeJoin.get("department").get("id"), departmentId));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        criteriaQuery.orderBy(criteriaBuilder.desc(ceoRoot.get("score")));

        List<EmployeeRatingDTO> ratingDTOS = entityManager.createQuery(criteriaQuery).getResultList();
        ratingDTOS.forEach(item -> item.setProfileImgUrl(employeeManagementService.getProfilePicture(item.getId())));

        Pagination pagination = setupPaging(ratingDTOS.size(), pageNo, pageSize);
        return new EmployeeRatingPagination(ratingDTOS, pagination);
    }

    @Override
    public List<SimpleItemDTO> getSkillSetByPosition(Integer positionId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SimpleItemDTO> criteriaQuery = criteriaBuilder.createQuery(SimpleItemDTO.class);

        Root<PositionSkillSet> positionSkillSetRoot = criteriaQuery.from(PositionSkillSet.class);
        Join<PositionSkillSet, SkillSet> skillSetJoin = positionSkillSetRoot.join("skillSet");

        criteriaQuery.multiselect(skillSetJoin.get("id"), skillSetJoin.get("skillSetName").alias("name"));

        Predicate predicate = criteriaBuilder.equal(positionSkillSetRoot.get("position").get("id"), positionId);
        criteriaQuery.where(predicate);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<HeatmapItemDTO> getDepartmentSkillSetHeatmap(Integer departmentId, Integer cycleId,
                                                             List<Integer> employeeIds, List<Integer> skillSetIds) {
        proficiencyLevelRepository.findAll();

        List<SimpleItemDTO> employees = getEmployeeSimpleItemDTOS(employeeIds);

        List<SimpleItemDTO> skillSets = getSkillSetSimpleItemDTOS(skillSetIds);

        List<SkillSetEvaluation> ssEvaluates = getSSEvalByEmployeeAndCycle(cycleId, employeeIds);

        return employees.stream()
                .flatMap(employee -> skillSets.stream()
                        .map(skillSet -> new Pair<>(employee, skillSet)))
                .map(pair -> getSkillSetScore(ssEvaluates, pair))
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
    private List<SimpleItemDTO> getSkillSetSimpleItemDTOS(List<Integer> skillSetIds) {
        Specification<SkillSet> hasSkillSetIds = GlobalSpec.hasIds(skillSetIds);
        return skillSetRepository.findAll(hasSkillSetIds)
                .stream()
                .map(skillSet -> new SimpleItemDTO(skillSet.getId(), skillSet.getSkillSetName()))
                .toList();
    }

    @NotNull
    private List<SkillSetEvaluation> getSSEvalByEmployeeAndCycle(Integer cycleId, List<Integer> employeeIds) {
        Specification<SkillSetEvaluation> hasCycleId = GlobalSpec.hasCompCycleId(cycleId);
        Specification<SkillSetEvaluation> hasEmployeeIds = GlobalSpec.hasEmployeeIds(employeeIds);

        return skillSetEvaluationRepository.findAll(hasCycleId.and(hasEmployeeIds));
    }

    private HeatmapItemDTO getSkillSetScore(List<SkillSetEvaluation> ssEvaluates,
                                            Pair<SimpleItemDTO, SimpleItemDTO> pair) {
        SimpleItemDTO employee = pair.getFirst();
        SimpleItemDTO skillSet = pair.getSecond();

        SkillSetEvaluation evaluationsHasEmployeeAndSkillSet = ssEvaluates.stream()
                .filter(ssEva -> ssEva.getEmployee().getId() == employee.getId()
                        && Objects.equals(ssEva.getSkillSet().getId(), skillSet.getId())).findFirst().orElse(null);

        float score = evaluationsHasEmployeeAndSkillSet == null ? 0 :
                evaluationsHasEmployeeAndSkillSet.getFinalProficiencyLevel().getScore();

        return new HeatmapItemDTO(employee.getName(), skillSet.getName(), score);
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
            Float score = compEvaluate == null ? 0 : compEvaluate.getFinalEvaluation();
            return new RadarValueDTO(employeeId, competencyId, score);
        }).toList();
    }

    private List<CompetencyEvaluation> findByCycleAndEmployees(Integer cycleId, List<Integer> employeeIds) {
        Specification<CompetencyEvaluation> hasCompetencyCycles = GlobalSpec.hasCompCycleId(cycleId);
        Specification<CompetencyEvaluation> hasEmployeeDepartment = GlobalSpec.hasEmployeeIds(employeeIds);
        return competencyEvaluationRepository.findAll(hasCompetencyCycles.and(hasEmployeeDepartment));
    }

}