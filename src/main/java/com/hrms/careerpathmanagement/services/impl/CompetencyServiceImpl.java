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
import com.hrms.employeemanagement.dto.NameImageDTO;
import com.hrms.employeemanagement.projection.NameAndStatusOnly;
import com.hrms.employeemanagement.dto.pagination.EmployeeRatingPagination;
import com.hrms.employeemanagement.dto.pagination.EmployeeStatusPagination;
import com.hrms.employeemanagement.models.*;
import com.hrms.employeemanagement.projection.ProfileImageOnly;
import com.hrms.employeemanagement.specification.EmployeeSpecification;
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
        return skillSetEvaluationRepository.findAll(empSpec.and(getCycleSpec(cycleId)));
    }

    @NotNull
    private static <T> Specification<T> getCycleSpec(Integer cycleId) {
        return (root, query, builder) -> builder.equal(root.get("competencyCycle").get("id"), cycleId);
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
        var skillSetAvgScore = getAverageSkillSet(employeeId, cycleId);

        //2. Skill Set Target Score
        var position = getPosition(employeeId);
        var level = getLevel(employeeId);
        var skillSetBaselineScore = careerManagementService.getBaselineSkillSetAvgScore(position.getId(), level.getId());

        DataItemDTO current = new DataItemDTO("Current", skillSetAvgScore.orElse(null));
        DataItemDTO target = new DataItemDTO("Target", skillSetBaselineScore.orElse(null));
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
        Specification<CompetencyEvaluationOverall> spec = (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("competencyCycle").get("id"), cycleId),
                criteriaBuilder.equal(root.get("employee").get("department").get("id"), departmentId)
        );

        var evaluations = evaluationOverallRepository.findAll(spec);

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

        var profileImages = employeeDamInfoRepository.findByIdSetAndType(empIdsSet, PROFILE_IMAGE);

        var result = nameAndStatusList.stream()
                .map(item -> new EmployeeStatusDTO(item.id(), item.firstName(), item.lastName(), item.status(),
                        profileImages.stream()
                                .filter(profile -> profile.employeeId().equals(item.id()))
                                .map(p -> p.url())
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
    public List<CompetencyTimeLine> getCompetencyTimeline(Integer competencyCycleId) {
        Specification<CompetencyTimeLine> spec =
                (root, query, cb) -> cb.equal(root.get("competencyCycle").get("id"), competencyCycleId);
        return competencyTimeLineRepository.findAll(spec);
    }

    @Override
    public MultiBarChartDTO getDepartmentIncompletePercent(Integer competencyCycleId) {
        List<Department> departments = departmentRepository.findAll();

        //Get all CompetencyEvaluationOverall of all employees have final status is agreed and get the latest cycle


        List<Float> selfData = processDepartmentData(departments, SELF_EVAL_LABEL_NAME, competencyCycleId);
        List<Float> managerData = processDepartmentData(departments, SUPERVISOR_EVAL_LABEL_NAME, competencyCycleId);

        List<MultiBarChartDataDTO> datasets = new ArrayList<>();
        datasets.add(new MultiBarChartDataDTO(SELF_EVAL_LABEL_NAME, selfData));
        datasets.add(new MultiBarChartDataDTO(SUPERVISOR_EVAL_LABEL_NAME, managerData));

        List<String> labels = departments.stream().map(Department::getDepartmentName).toList();
        return new MultiBarChartDTO(labels, datasets);
    }

    private List<Float> processDepartmentData(List<Department> departments, String type, Integer competencyCycleId) {
        return departments.parallelStream().map(item -> {
            List<Integer> empIdSet = employeeManagementService
                    .findEmployees(item.getId())
                    .stream()
                    .map(Employee::getId)
                    .toList();

            return (type.equals(SELF_EVAL_LABEL_NAME))
                    ? getEmployeeInCompletedPercent(competencyCycleId, empIdSet)
                    : getEvaluatorInCompletePercent(competencyCycleId, empIdSet);
        }).toList();
    }

    private Float getEvaluatorInCompletePercent(Integer competencyCycleId, List<Integer> empIdSet) {
        return calculatePercentage(competencyCycleId, empIdSet, "evaluatorStatus");
    }

    private Float getEmployeeInCompletedPercent(Integer competencyCycleId, List<Integer> empIdSet) {
        return calculatePercentage(competencyCycleId, empIdSet, "employeeStatus");
    }

    private Float calculatePercentage(Integer competencyCycleId, List<Integer> empIdSet, String roleField) {
        if (empIdSet.isEmpty()) return null;
        Specification<CompetencyEvaluationOverall> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.and(root.get("employee").get("id").in(empIdSet),
                        criteriaBuilder.equal(root.get(roleField), "Completed"),
                        criteriaBuilder.equal(root.get("competencyCycle").get("id"), competencyCycleId));

        var completedCount = evaluationOverallRepository.count(specification);
        return (float) (empIdSet.size() - completedCount) / empIdSet.size() * 100;
    }

    @Override
    public PieChartDTO getCompanyIncompletePercent(Integer competencyCycleId) {

        List<Integer> empIdSet = employeeManagementService.getAllEmployees()
                .stream()
                .map(Employee::getId)
                .toList();

        //get all employees who have completed evaluation
        Specification<CompetencyEvaluationOverall> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.and(root.get("employee").get("id").in(empIdSet),
                        criteriaBuilder.equal(root.get("finalStatus"), "Completed"),
                        criteriaBuilder.equal(root.get("competencyCycle").get("id"), competencyCycleId)
                );

        List<Float> datasets = new ArrayList<>();
        var completedPercent = (float) evaluationOverallRepository.count(spec) / empIdSet.size() * 100;
        datasets.add(completedPercent);
        datasets.add(100 - completedPercent);

        return new PieChartDTO(List.of(COMPLETED_LABEL_NAME, IN_COMPLETED_LABEL_NAME), datasets);
    }


    @Override
    public List<HeatmapItemDTO> getHeatmapCompetency(Integer positionId, Integer competencyCycleId) {
        List<CompetencyEvaluation> compEvaluates = fetchCompetencyEvaluations(positionId, competencyCycleId);
        List<JobLevel> jobLevelIds = jobLevelRepository.findAll();
        List<Competency> competencyIds = competencyRepository.findAll();

        return jobLevelIds.stream()
                .flatMap(jobLevel -> competencyIds.stream()
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
    public DataItemPagingDTO getHighestSkillSet(@Nullable Integer employeeId,
                                                Integer competencyCycleId, int pageNo, int pageSize) {
        CompetencyCycle evalLatestCycle = evaluationOverallRepository.latestEvalCompetencyCycle(employeeId);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        skillSetRepository.findAll();
        proficiencyLevelRepository.findAll();

        Specification<SkillSetEvaluation> spec = employeeId == null
                ?
                (root, query, builder) -> {
                    query.orderBy(builder.desc(root.get("finalProficiencyLevel")));
                    query.where(builder.equal(root.get("competencyCycle").get("id"), competencyCycleId));
                    return query.getRestriction();
                }
                :
                (root, query, builder) -> {
                    query.orderBy(builder.desc(root.get("finalProficiencyLevel")));
                    query.where(builder.equal(root.get("competencyCycle").get("id"), evalLatestCycle.getId()),
                            builder.equal(root.get("employee").get("id"), employeeId));
                    return query.getRestriction();
                };

        Page<DataItemDTO> ssEvaluates = skillSetEvaluationRepository
                .findAll(spec, pageable)
                .map(item -> new DataItemDTO(item.getSkillSet().getSkillSetName(),
                        item.getFinalProficiencyLevel().getScore()));
        Pagination pagination = setupPaging(ssEvaluates.getTotalElements(), pageNo, pageSize);
        return new DataItemPagingDTO(ssEvaluates.getContent(), pagination);
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
    public DataItemPagingDTO getTopHighestSkillSetTargetEmployee(Integer employeeId, int pageNo, int pageSize) {
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
        Specification<PerformanceEvaluation> hasEmployeeId2 = performanceSpecification.hasEmployeeId(employeeId);
        Specification<PerformanceEvaluation> hasPerformanceCycleIds = performanceSpecification.hasPerformanceCycleId(latestPerformCycle.getPerformanceCycleId());
        PerformanceEvaluation perfEval = performanceEvaluationRepository
                .findOne(hasEmployeeId2.and(hasPerformanceCycleIds))
                .orElse(null);
        return perfEval == null
                ? new CurrentEvaluationDTO(latestPerformCycle.getPerformanceCycleName(), "Not Started", null)
                : new CurrentEvaluationDTO(perfEval.getPerformanceCycle().getPerformanceCycleName(),
                perfEval.getStatus(), perfEval.getLastUpdated().toString());
    }

    @NotNull
    private CurrentEvaluationDTO getCurrentCompEval(Integer employeeId) {
        Specification<CompetencyEvaluationOverall> hasEmployeeId = employeeSpecification.hasEmployeeId(employeeId);
        Specification<CompetencyEvaluationOverall> hasCompetencyCycleIds = competencySpecification.hasCycleId(latestCompCycle.getId());
        CompetencyEvaluationOverall evalOvr = evaluationOverallRepository
                .findOne(hasEmployeeId.and(hasCompetencyCycleIds))
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
        Specification<CompetencyEvaluationOverall> spec = (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("finalStatus"), "Completed"),
                criteriaBuilder.equal(root.get("competencyCycle").get("id"), cycleId)
        );
        List<Float> evalScores = evaluationOverallRepository.findAll(spec)
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
        Specification<SkillSetTarget> ssTSpec = (root, query, builder) -> builder.and(
                builder.equal(root.get("employee").get("id"), employeeId),
                builder.equal(root.get("competencyCycle").get("id"), latestCompId)
        );
        return skillSetTargetRepository.findAll(ssTSpec);
    }

    private List<PositionSkillSet> getPositionSkillSets(Integer positionId, List<Integer> competencyId) {
        //Find PositionSkillSet has positionId = positionId and competencyId in competencyIds
        Specification<PositionSkillSet> posSpec = (root, query, builder) -> builder.and(
                builder.equal(root.get("position").get("id"), positionId),
                root.get("skillSet").get("competency").get("id").in(competencyId)
        );
        return positionSkillSetRepository.findAll(posSpec);
    }

    private List<SkillSetEvaluation> getSkillSetEvaluations(Integer employeeId, Integer cycleId) {
        Specification<SkillSetEvaluation> ssEvaSpec = (root, query, builder) -> builder.and(
                builder.equal(root.get("employee").get("id"), employeeId),
                builder.equal(root.get("competencyCycle").get("id"), cycleId)
        );
        return skillSetEvaluationRepository.findAll(ssEvaSpec);
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
        Specification<CompetencyEvaluationOverall> spec = (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("employee").get("id"), empId),
                criteriaBuilder.equal(root.get("competencyCycle").get("id"), latestCompEva)
        );

        CompetencyEvaluationOverall eval = evaluationOverallRepository.findOne(spec).orElse(null);
        Employee employee = employeeManagementService.findEmployee(empId);
        return SkillMatrixOverallDTO.builder()
                .managerName(employee.getDepartment().getSum().getFullName())
                .status(Objects.requireNonNull(eval).getFinalStatus()).build();
    }

    @Override
    public RadarChartDTO getCompetencyRadarChart(List<Integer> competencyCyclesId, Integer departmentId) {
        return null;
    }

    @Override
    public List<EvaluationCycleInfoDTO> getEvaluationCycles() {
        List<EvaluationCycleInfoDTO> evaluationCycleInfoDTOS = new ArrayList<>();
        List<CompetencyEvaluationOverall> compEvalOverall = evaluationOverallRepository.findAll();
        long totalEmp = employeeManagementService.getAllEmployees().size();

        //Handle Competency Cycle
        List<CompetencyCycle> compCycles = competencyCycleRepository.findAll();
        compCycles.forEach(item -> evaluationCycleInfoDTOS.add(new EvaluationCycleInfoDTO(
                item.getCompetencyCycleName(),
                item.getStatus(),
                String.format("%s - %s", item.getStartDate().toString(), item.getDueDate().toString()),
                "Competencies Evaluation",
                new CycleEvaluationProgressDTO(getCompletedEvalPercentage(compEvalOverall, item.getId(), totalEmp),
                        getSelfCompletedEvalPercentage(compEvalOverall, item.getId(), totalEmp),
                        managerCompletedEvalPercentage(compEvalOverall, item.getId(), totalEmp))
        )));

        //Handle Performance Cycle
        List<PerformanceCycle> perfCycles = performanceCycleRepository.findAll();
        perfCycles.forEach(item -> evaluationCycleInfoDTOS.add(new EvaluationCycleInfoDTO(
                item.getPerformanceCycleName(),
                item.getStatus(),
                String.format("%s - %s",
                        item.getPerformanceCycleStartDate().toString(),
                        item.getPerformanceCycleEndDate().toString()),
                "Performance Evaluation",
                new CycleEvaluationProgressDTO()
        )));
        //Get all Cycle information
        return evaluationCycleInfoDTOS;
    }


    private Float getCompletedEvalPercentage(List<CompetencyEvaluationOverall> compEvalOverall,
                                             Integer compCycleId, long totalEmp) {
        List<CompetencyEvaluationOverall> compEvalOverallFilter = compEvalOverall.stream()
                .filter(item -> item.getCompetencyCycle().getId() == compCycleId)
                .toList();

        long totalEmpCompleted = compEvalOverallFilter.stream()
                .filter(item -> item.getFinalStatus().equals("Agreed"))
                .count();
        return (float) totalEmpCompleted / totalEmp * 100;
    }

    private Float getSelfCompletedEvalPercentage(List<CompetencyEvaluationOverall> compEvalOverall,
                                                 Integer compCycleId, long totalEmp) {
        List<CompetencyEvaluationOverall> compEvalOverallFilter = compEvalOverall.stream()
                .filter(item -> item.getCompetencyCycle().getId() == compCycleId)
                .toList();

        long totalEmpCompleted = compEvalOverallFilter.stream()
                .filter(item -> item.getEmployeeStatus().equals("Completed"))
                .count();
        return (float) totalEmpCompleted / totalEmp * 100;
    }

    private Float managerCompletedEvalPercentage(List<CompetencyEvaluationOverall> compEvalOverall,
                                                 Integer compCycleId, long totalEmp) {
        List<CompetencyEvaluationOverall> compEvalOverallFilter = compEvalOverall.stream()
                .filter(item -> item.getCompetencyCycle().getId() == compCycleId)
                .toList();

        long totalEmpCompleted = compEvalOverallFilter.stream()
                .filter(item -> item.getEvaluatorStatus().equals("Completed"))
                .count();
        return (float) totalEmpCompleted / totalEmp * 100;
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
    public EmployeeRatingPagination getCompetencyRating(Integer cycleId, PageRequest pageable) {
        Specification<CompetencyEvaluationOverall> hasCycleSpec = competencySpecification.hasCycleId(cycleId);
        Sort sort = Sort.by(Sort.Direction.DESC, "score");

        var empRatings = evaluationOverallRepository.findAll(hasCycleSpec, sort)
                .stream().map(item -> new EmployeeRatingDTO(
                        item.getEmployee().getId(),
                        item.getEmployee().getFirstName(),
                        item.getEmployee().getLastName(),
                        employeeManagementService.getProfilePicture(item.getEmployee().getId()),
                        item.getScore()
                )).toList();

        var pagination = setupPaging(empRatings.size(), pageable.getPageNumber(), pageable.getPageSize());

        return new EmployeeRatingPagination(empRatings, pagination);
    }

}