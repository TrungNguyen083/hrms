package com.hrms.global.services.impl;

import com.hrms.careerpathmanagement.dto.TimeLine;
import com.hrms.careerpathmanagement.repositories.ProficiencyLevelRepository;
import com.hrms.employeemanagement.dto.SimpleItemDTO;
import com.hrms.employeemanagement.repositories.DepartmentRepository;
import com.hrms.employeemanagement.repositories.JobLevelRepository;
import com.hrms.employeemanagement.repositories.PositionRepository;
import com.hrms.global.GlobalSpec;
import com.hrms.global.models.*;
import com.hrms.global.services.CompanyCoreService;
import com.hrms.performancemanagement.repositories.EvaluateCycleRepository;
import com.hrms.performancemanagement.repositories.EvaluateTimeLineRepository;
import com.hrms.usermanagement.model.Role;
import com.hrms.usermanagement.repository.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Autowired
    public CompanyCoreServiceImpl(
            ProficiencyLevelRepository proficiencyLevelRepository,
            DepartmentRepository departmentRepository,
            JobLevelRepository jobLevelRepository,
            PositionRepository positionRepository,
            RoleRepository roleRepository,
            EvaluateTimeLineRepository evaluateTimeLineRepository,
            EvaluateCycleRepository evaluateCycleRepository
            ) {
        this.proficiencyLevelRepository = proficiencyLevelRepository;
        this.departmentRepository = departmentRepository;
        this.jobLevelRepository = jobLevelRepository;
        this.positionRepository = positionRepository;
        this.roleRepository = roleRepository;
        this.evaluateTimeLineRepository = evaluateTimeLineRepository;
        this.evaluateCycleRepository = evaluateCycleRepository;
    }

    @Override
    public List<ProficiencyLevel> getProficiencyLevels() {
        return proficiencyLevelRepository.findAll();
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
        //Sort by initialDate DESC
        Sort sort = Sort.by("initialDate").descending();
        return evaluateCycleRepository.findAll(sort);
    }

    @Override
    public List<TimeLine> getEvaluateTimeline(Integer evaluateCycleId) {
        Specification<EvaluateTimeLine> spec = GlobalSpec.hasEvaluateCycleId(evaluateCycleId);
        return evaluateTimeLineRepository.findAll(spec)
                .stream()
                .map(item -> new TimeLine(
                        item.getEvaluateTimeLineName(),
                        item.getStartDate().toString(), item.getDueDate().toString(),
                        item.getIsDone()))
                .toList();
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
}
