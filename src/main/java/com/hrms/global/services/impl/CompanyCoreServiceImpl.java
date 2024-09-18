package com.hrms.global.services.impl;

import com.hrms.careerpathmanagement.dto.TimeLine;
import com.hrms.careerpathmanagement.input.EvaluateCycleInput;
import com.hrms.careerpathmanagement.models.CompetencyEvaluationOverall;
import com.hrms.careerpathmanagement.repositories.ProficiencyLevelRepository;
import com.hrms.employeemanagement.dto.SimpleItemDTO;
import com.hrms.employeemanagement.repositories.DepartmentRepository;
import com.hrms.employeemanagement.repositories.JobLevelRepository;
import com.hrms.employeemanagement.repositories.PositionRepository;
import com.hrms.global.GlobalSpec;
import com.hrms.global.mapper.HrmsMapper;
import com.hrms.global.models.*;
import com.hrms.global.services.CompanyCoreService;
import com.hrms.performancemanagement.input.PerformanceRangeInput;
import com.hrms.performancemanagement.input.ProficiencyLevelInput;
import com.hrms.performancemanagement.model.PerformanceRange;
import com.hrms.performancemanagement.repositories.EvaluateCycleRepository;
import com.hrms.performancemanagement.repositories.EvaluateTimeLineRepository;
import com.hrms.performancemanagement.repositories.PerformanceRangeRepository;
import com.hrms.usermanagement.model.Role;
import com.hrms.usermanagement.repository.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
@Transactional
@Slf4j
public class CompanyCoreServiceImpl implements CompanyCoreService {
    @PersistenceContext
    EntityManager entityManager;
    private final ProficiencyLevelRepository proficiencyLevelRepository;
    private final DepartmentRepository departmentRepository;
    private final JobLevelRepository jobLevelRepository;
    private final PositionRepository positionRepository;
    private final RoleRepository roleRepository;
    private final EvaluateTimeLineRepository evaluateTimeLineRepository;
    private final EvaluateCycleRepository evaluateCycleRepository;
    private final PerformanceRangeRepository performanceRangeRepository;
    private final HrmsMapper modelMapper;

    @Autowired
    public CompanyCoreServiceImpl(
            ProficiencyLevelRepository proficiencyLevelRepository,
            DepartmentRepository departmentRepository,
            JobLevelRepository jobLevelRepository,
            PositionRepository positionRepository,
            RoleRepository roleRepository,
            HrmsMapper modelMapper,
            EvaluateTimeLineRepository evaluateTimeLineRepository,
            EvaluateCycleRepository evaluateCycleRepository,
            PerformanceRangeRepository performanceRangeRepository
    ) {
        this.proficiencyLevelRepository = proficiencyLevelRepository;
        this.departmentRepository = departmentRepository;
        this.jobLevelRepository = jobLevelRepository;
        this.positionRepository = positionRepository;
        this.roleRepository = roleRepository;
        this.evaluateTimeLineRepository = evaluateTimeLineRepository;
        this.evaluateCycleRepository = evaluateCycleRepository;
        this.performanceRangeRepository = performanceRangeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ProficiencyLevel> getProficiencyLevels() {
        return proficiencyLevelRepository.findAll();
    }

    @Override
    public List<PerformanceRange> getPerformanceRanges() {
        return performanceRangeRepository.findAll();
    }

    @Override
    public List<Department> getDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Long getNumberOfDepartments() {
        return departmentRepository.count();
    }

    @Override
    public List<JobLevel> getJobLevels() {
        return jobLevelRepository.findAll();
    }

    @Override
    public List<Position> getPositions() {
        return positionRepository.findAll();
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public List<EvaluateCycle> getEvaluateCycles() {
        // Specification to filter out cycles with status "Not Start"
        Specification<EvaluateCycle> filterOutNotStart = (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("status"), "Not Start");

        // Sort by initialDate in descending order
        Sort sort = Sort.by(Sort.Direction.DESC, "initialDate");

        // Find all cycles applying both the specification and sort
        return evaluateCycleRepository.findAll(Specification.where(filterOutNotStart), sort);
    }


    @Override
    public List<TimeLine> getEvaluateTimeline(Integer evaluateCycleId) {
        Specification<EvaluateTimeLine> spec = GlobalSpec.hasEvaluateCycleId(evaluateCycleId);
        return evaluateTimeLineRepository.findAll(spec).stream().map(item -> new TimeLine(item.getEvaluateTimeLineName(), item.getStartDate().toString(), item.getDueDate().toString(), item.getIsDone())).toList();
    }

    @Override
    public List<SimpleItemDTO> getPositionLevelSkills(Integer positionId, Integer jobLevelId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SimpleItemDTO> criteriaQuery = criteriaBuilder.createQuery(SimpleItemDTO.class);

        Root<PositionLevelSkill> positionSkillRoot = criteriaQuery.from(PositionLevelSkill.class);
        Join<PositionLevelSkill, Skill> skillJoin = positionSkillRoot.join("skill");

        criteriaQuery.multiselect(skillJoin.get("id"), skillJoin.get("skillName").alias("name"));

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(positionSkillRoot.get("position").get("id"), positionId));
        predicates.add(criteriaBuilder.equal(positionSkillRoot.get("jobLevel").get("id"), jobLevelId));

        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Transactional
    @Override
    public Boolean createEvaluationCycle(EvaluateCycleInput input) {
        EvaluateCycle cycle = parseCycle(input);
        List<EvaluateTimeLine> timeLines = parseTimeLines(input, cycle);

        try {
            evaluateCycleRepository.save(cycle);
            evaluateTimeLineRepository.saveAll(timeLines);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private EvaluateCycle parseCycle(EvaluateCycleInput input) {
        EvaluateCycle cycle = modelMapper.map(input, EvaluateCycle.class);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cycle.getStartDate());
        int year = calendar.get(Calendar.YEAR);
        cycle.setYear(year);

        return cycle;
    }

    private List<EvaluateTimeLine> parseTimeLines(EvaluateCycleInput input, EvaluateCycle cycle) {

        List<EvaluateTimeLine> timeLines = input.getTimeLines().stream().map(tl -> modelMapper.map(tl, EvaluateTimeLine.class)).toList();
        timeLines.forEach(tl -> tl.setEvaluateCycle(cycle));

        return timeLines;
    }

    @Transactional
    @Override
    public Boolean updateProficiencyLevel(Integer id, ProficiencyLevelInput input) {
        ProficiencyLevel proficiencyLevel = proficiencyLevelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proficiency level not found"));
        proficiencyLevel.setProficiencyLevelName(input.getName());
        proficiencyLevel.setProficiencyLevelDescription(input.getDescription());
        proficiencyLevel.setScore(input.getScore());
        proficiencyLevelRepository.save(proficiencyLevel);
        return Boolean.TRUE;
    }

    @Transactional
    @Override
    public Boolean updatePerformanceRange(Integer id, PerformanceRangeInput input) {
        PerformanceRange performanceRange = performanceRangeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Performance range not found"));
        performanceRange.setMinValue(input.getMinValue());
        performanceRange.setMaxValue(input.getMaxValue());
        performanceRange.setText(input.getText());
        performanceRange.setDescription(input.getDescription());
        performanceRange.setOrdered(input.getOrdered());
        performanceRangeRepository.save(performanceRange);
        return Boolean.TRUE;
    }

    @Transactional
    @Override
    public Boolean createProficiencyLevel(ProficiencyLevelInput input) {
        ProficiencyLevel proficiencyLevel = ProficiencyLevel.builder()
                .proficiencyLevelName(input.getName())
                .proficiencyLevelDescription(input.getDescription())
                .score(input.getScore())
                .build();
        proficiencyLevelRepository.save(proficiencyLevel);
        return Boolean.TRUE;
    }

    @Transactional
    @Override
    public Boolean createPerformanceRange(PerformanceRangeInput input) {
        PerformanceRange performanceRange = PerformanceRange.builder()
                .text(input.getText())
                .description(input.getDescription())
                .minValue(input.getMinValue())
                .maxValue(input.getMaxValue())
                .ordered(input.getOrdered())
                .build();
        performanceRangeRepository.save(performanceRange);
        return Boolean.TRUE;
    }

    @Override
    public Boolean deleteProficiencyLevel(Integer id) {
        ProficiencyLevel proficiencyLevel = proficiencyLevelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proficiency level not found"));
        proficiencyLevelRepository.delete(proficiencyLevel);
        return Boolean.TRUE;
    }

    @Override
    public Boolean deletePerformanceRange(Integer id) {
        PerformanceRange performanceRange = performanceRangeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Performance range not found"));
        performanceRangeRepository.delete(performanceRange);
        return Boolean.TRUE;
    }
}
