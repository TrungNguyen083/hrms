package com.hrms.careerpathmanagement.services.impl;

import com.hrms.careerpathmanagement.dto.CompareOverview;
import com.hrms.careerpathmanagement.dto.EvaluationPromotion;
import com.hrms.careerpathmanagement.dto.pagination.PromotionPaging;
import com.hrms.careerpathmanagement.models.EmployeeCareerPath;
import com.hrms.careerpathmanagement.models.Promotion;
import com.hrms.careerpathmanagement.models.SkillEvaluation;
import com.hrms.careerpathmanagement.repositories.EmployeeCareerPathRepository;
import com.hrms.careerpathmanagement.repositories.PromotionRepository;
import com.hrms.careerpathmanagement.repositories.SkillEvaluationRepository;
import com.hrms.careerpathmanagement.services.CareerService;
import com.hrms.employeemanagement.models.Employee;
import com.hrms.employeemanagement.projection.ProfileImageOnly;
import com.hrms.employeemanagement.repositories.EmployeeDamInfoRepository;
import com.hrms.employeemanagement.repositories.EmployeeRepository;
import com.hrms.employeemanagement.repositories.JobLevelRepository;
import com.hrms.employeemanagement.repositories.PositionRepository;
import com.hrms.employeemanagement.services.EmployeeManagementService;
import com.hrms.global.GlobalSpec;
import com.hrms.global.models.EvaluateCycle;
import com.hrms.global.models.JobLevel;
import com.hrms.global.models.Position;
import com.hrms.global.models.PositionLevelSkill;
import com.hrms.global.paging.Pagination;
import com.hrms.global.paging.PaginationSetup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CareerServiceImpl implements CareerService {
    @PersistenceContext
    EntityManager em;
    static String PROFILE_IMAGE = "PROFILE_IMAGE";
    static String APPROVED = "Approved";
    static String REJECTED = "Rejected";
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final JobLevelRepository jobLevelRepository;
    private final SkillEvaluationRepository skillEvaluationRepository;
    private final PromotionRepository promotionRepository;
    private final EmployeeCareerPathRepository employeeCareerPathRepository;
    private final EmployeeManagementService employeeManagementService;
    private final EmployeeDamInfoRepository employeeDamInfoRepository;


    @Autowired
    public CareerServiceImpl(
            PositionRepository positionRepository,
            JobLevelRepository jobLevelRepository,
            EmployeeRepository employeeRepository,
            PromotionRepository promotionRepository,
            SkillEvaluationRepository skillEvaluationRepository,
            EmployeeDamInfoRepository employeeDamInfoRepository,
            EmployeeCareerPathRepository employeeCareerPathRepository,
            EmployeeManagementService employeeManagementService
    ) {
        this.positionRepository = positionRepository;
        this.jobLevelRepository = jobLevelRepository;
        this.employeeRepository = employeeRepository;
        this.promotionRepository = promotionRepository;
        this.skillEvaluationRepository = skillEvaluationRepository;
        this.employeeDamInfoRepository = employeeDamInfoRepository;
        this.employeeCareerPathRepository = employeeCareerPathRepository;
        this.employeeManagementService = employeeManagementService;
    }

    public Float getMatchPercent(Integer employeeId, Integer positionId, Integer levelId) {
        var baselineSkillSetIds = getBaselineSkillSetIds(positionId, levelId);
        var currentSkillSetIds = getCurrentSkillSetIds(employeeId);

        var intersectSkillsSet = currentSkillSetIds.stream()
                .filter(baselineSkillSetIds::contains)
                .toList();

        return (float) 100 * intersectSkillsSet.size() / baselineSkillSetIds.size();
    }

    private List<Integer> getBaselineSkillSetIds(Integer positionId, Integer levelId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        var query = cb.createQuery(Integer.class);
        var root = query.from(PositionLevelSkill.class);

        query.select(root.get("skillSet").get("id")).where(cb.and(
                cb.equal(root.get("position").get("id"), positionId),
                cb.equal(root.get("jobLevel").get("id"), levelId)
        )).distinct(true);

        return em.createQuery(query).getResultList();
    }

    private List<Integer> getCurrentSkillSetIds(Integer employeeId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        var query = cb.createQuery(Integer.class);
        var root = query.from(SkillEvaluation.class);

        query.select(root.get("skillSet").get("id"))
                .where(cb.equal(root.get("employee").get("id"), employeeId))
                .distinct(true);

        return em.createQuery(query).getResultList();
    }

    @Override
    public Boolean createRequestPromotion(List<Integer> employeeIds, Integer cycleId) {
        List<Integer> newEmployeeIds = employeeIds.stream()
                .filter(e -> {
                    Specification<Promotion> hasId = GlobalSpec.hasEmployeeId(e);
                    Specification<Promotion> hasCycle = GlobalSpec.hasEvaluateCycleId(cycleId);
                    return !promotionRepository.exists(hasId.and(hasCycle));
                })
                .toList();

        List<Promotion> promotions = newEmployeeIds.stream().map(eId -> Promotion.builder()
                .employee(new Employee(eId))
                .evaluateCycle(new EvaluateCycle(cycleId))
                .status("Pending")
                .build()).toList();

        promotionRepository.saveAll(promotions);
        return Boolean.TRUE;
    }

    @Override
    public PromotionPaging getPromotionList(Integer cycleId, String name, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<Promotion> promotions = promotionRepository.findAll(pageable);

        List<EvaluationPromotion> data = promotions.stream().map(pr -> {
            String profileImage = employeeManagementService.getProfilePicture(pr.getEmployee().getId());

            List<SkillEvaluation> ssEvaluates = getSkillEvaluations(pr.getEmployee().getId(), cycleId);
            EmployeeCareerPath careerPath = getEmployeeCareerPath(pr.getEmployee().getId(), cycleId);

            return EvaluationPromotion.builder()
                    .employeeId(pr.getEmployee().getId())
                    .profileImage(profileImage)
                    .firstName(pr.getEmployee().getFirstName())
                    .lastName(pr.getEmployee().getLastName())
                    .currentPositionLevel(careerPath.getJobLevel().getJobLevelName()
                            + " " + careerPath.getPosition().getPositionName())
                    .promotePositionLevel(getNextPositionLevel(careerPath.getPosition().getId(),
                            careerPath.getJobLevel().getId(),
                            ssEvaluates)
                    )
                    .status(pr.getStatus())
                    .build();
        }).toList();

        Pagination pagination = PaginationSetup.setupPaging(promotions.getTotalElements(), pageNo, pageSize);
        return new PromotionPaging(data, pagination);
    }

    private List<SkillEvaluation> getSkillEvaluations(Integer employeeId, Integer cycleId) {
        Specification<SkillEvaluation> hasEmployee = GlobalSpec.hasEmployeeId(employeeId);
        Specification<SkillEvaluation> hasCycle = GlobalSpec.hasEvaluateCycleId(cycleId);

        return skillEvaluationRepository.findAll(hasEmployee.and(hasCycle));
    }

    private EmployeeCareerPath getEmployeeCareerPath(Integer employeeId, Integer cycleId) {
        Specification<EmployeeCareerPath> hasEmployee = GlobalSpec.hasEmployeeId(employeeId);
        Specification<EmployeeCareerPath> hasCycle = GlobalSpec.hasEvaluateCycleId(cycleId);

        return employeeCareerPathRepository.findAll(hasEmployee.and(hasCycle)).stream().findFirst().orElseThrow();
    }

    private Promotion getEmployeePromotion(Integer employeeId, Integer cycleId) {
        Specification<Promotion> hasEmployee = GlobalSpec.hasEmployeeId(employeeId);
        Specification<Promotion> hasCycle = GlobalSpec.hasEvaluateCycleId(cycleId);

        return promotionRepository.findAll(hasEmployee.and(hasCycle)).stream().findFirst().orElseThrow();
    }

    private String getNextPositionLevel(Integer position, Integer jobLevel,
                                        List<SkillEvaluation> ssEvaluates) {
        int newJobLevel = getNextJobLevel(jobLevel);
        int newPosition = getNextPosition(position, newJobLevel, ssEvaluates);
        Position p = positionRepository.findById(newPosition).orElseThrow();
        JobLevel jL = jobLevelRepository.findById(newJobLevel).orElseThrow();

        return jL.getJobLevelName() + " " + p.getPositionName();
    }

    private Integer getNextJobLevel(Integer jobLevel) {
        return (jobLevel + 1) < 4 ? jobLevel + 1 : 1;
    }

    private Integer getNextPosition(Integer position, Integer newJobLevel, List<SkillEvaluation> ssEvaluates) {
        return (newJobLevel == 1)
                ? ssEvaluates.stream()
                .collect(Collectors.teeing(
                        Collectors.filtering(se -> se.getSkill().getId() >= 1 && se.getSkill().getId() <= 30,
                                Collectors.averagingDouble(SkillEvaluation::getFinalScore)),
                        Collectors.filtering(se -> se.getSkill().getId() > 30,
                                Collectors.averagingDouble(SkillEvaluation::getFinalScore)),
                        (avgSoftSkills, avgHardSkills) -> avgHardSkills > avgSoftSkills ? 12 : 6))
                : position;
    }

    @Override
    public List<CompareOverview> getCompareOverview(List<Integer> employeeIds, Integer cycleId) {
        List<Employee> employees = employeeRepository.findAll(GlobalSpec.hasIds(employeeIds));
        List<ProfileImageOnly> urls = employeeDamInfoRepository.findByEmployeeIdsSetAndFileType(employeeIds, PROFILE_IMAGE);

        Specification<SkillEvaluation> hasEmployees = GlobalSpec.hasEmployeeIds(employeeIds);
        Specification<SkillEvaluation> hasCycle = GlobalSpec.hasEvaluateCycleId(cycleId);

        List<SkillEvaluation> sEs = skillEvaluationRepository.findAll(hasEmployees.and(hasCycle));

        return employees.stream().map(e -> {
            EmployeeCareerPath careerPath = getEmployeeCareerPath(e.getId(), cycleId);

            String profileImage = urls.stream()
                    .filter(pI -> pI.getEmployeeId().equals(e.getId()))
                    .map(ProfileImageOnly::getUrl).findFirst().orElse(null);

            String currentPosition = careerPath.getJobLevel().getJobLevelName()
                    + " " + careerPath.getPosition().getPositionName();
            List<SkillEvaluation> filtersEs = sEs.stream()
                    .filter(sE -> sE.getEmployee().getId().equals(e.getId()))
                    .toList();

            String targetPosition = getNextPositionLevel(careerPath.getPosition().getId(),
                    careerPath.getJobLevel().getId(), filtersEs);

            return new CompareOverview(profileImage, e.getFirstName(), e.getLastName(),
                    currentPosition, targetPosition);
        }).toList();
    }

    @Override
    public Boolean updatePromotionRequest(Integer employeeId, Integer cycleId, Boolean isApprove, String comment) {
        Promotion promotion = getEmployeePromotion(employeeId, cycleId);

        promotion.setStatus((isApprove) ? APPROVED : REJECTED);
        promotion.setComment(comment);

        if (isApprove) {
            //update career path for next cycle

            //set new position for employee
            Employee employee = employeeRepository.findById(employeeId).orElseThrow();
            List<SkillEvaluation> ssEs = getSkillEvaluations(employeeId, cycleId);
            int newJobLevel = getNextJobLevel(employee.getJobLevel().getId());
            int newPosition = getNextPosition(employee.getPosition().getId(), newJobLevel, ssEs);
            employee.setJobLevel(new JobLevel(newJobLevel));
            employee.setPosition(new Position(newPosition));

            employeeRepository.save(employee);
        }

        promotionRepository.save(promotion);

        return Boolean.TRUE;
    }

}
