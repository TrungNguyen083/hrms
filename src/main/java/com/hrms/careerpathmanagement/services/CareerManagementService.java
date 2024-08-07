package com.hrms.careerpathmanagement.services;

import com.hrms.global.models.PositionLevelSkill;
import com.hrms.global.models.ProficiencyLevel;
import com.hrms.careerpathmanagement.models.SkillEvaluation;
import com.hrms.careerpathmanagement.repositories.PositionLevelSkillRepository;
import com.hrms.careerpathmanagement.repositories.PositionLevelPathRepository;
import com.hrms.careerpathmanagement.repositories.SkillEvaluationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CareerManagementService {
    @PersistenceContext
    EntityManager em;
    private final PositionLevelPathRepository positionLevelPathRepository;


    @Autowired
    public CareerManagementService(PositionLevelPathRepository positionLevelPathRepository,
                                   PositionLevelSkillRepository baselineSkillSetRepository,
                                   SkillEvaluationRepository skillEvaluationRepository) {
        this.positionLevelPathRepository = positionLevelPathRepository;
    }

//    public CareerPathTreeDTO getCareerPathTree(PositionCareerPath position) {
//        var root = getPositionLevelNode(position.rootPositionLevelId);
//        return new CareerPathTreeDTO(position.title, root);
//    }


    /**
     * A node has title and a list of next nodes
     * @param positionLevelId
     * @return
     */
//    public PositionLevelNodeDTO getPositionLevelNode(Integer positionLevelId) {
//        var title = positionLevelRepository.findById(positionLevelId).get().getTitle();
//
//        if (!positionLevelPathRepository.existsByCurrentId(positionLevelId))
//            return new PositionLevelNodeDTO(positionLevelId, title, null);
//
//        var node = new PositionLevelNodeDTO(positionLevelId, title, new LinkedList<>());
//
//        var nextPositionLevels = positionLevelPathRepository.findAllByCurrentId(positionLevelId)
//                .stream().map(PositionLevelPath::getNext)
//                .toList();
//
//        nextPositionLevels.forEach(i -> node.getNextPositionLevels().add(getPositionLevelNode(i.getId())));
//
//        return node;
//    }

    /***
     * Employee's skills set is store in SkillSetEvaluation table, but that employee's position can be changed over time
     * Example: Employee A is a Software Engineer, but he can be changed to Quality Assurance Engineer
     * @param employeeId
     * @param positionId
     * @param levelId
     * @return
     */
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

    public Optional<Double> getBaselineSkillAvgScore(Integer positionId, Integer levelId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Double> query = cb.createQuery(Double.class);
        Root<PositionLevelSkill> root = query.from(PositionLevelSkill.class);
        Join<PositionLevelSkill, ProficiencyLevel> proficencyJoin = root.join("proficiencyLevel");

        query.select(cb.avg(proficencyJoin.get("score")));
        query.where(cb.equal(root.get("position").get("id"), positionId),
                cb.equal(root.get("jobLevel").get("id"), levelId));

        return Optional.ofNullable(em.createQuery(query).getSingleResult());
    }
}
