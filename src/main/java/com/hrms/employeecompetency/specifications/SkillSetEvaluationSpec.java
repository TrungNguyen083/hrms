package com.hrms.employeecompetency.specifications;

import com.hrms.employeecompetency.models.SkillSetEvaluation;
import org.springframework.data.jpa.domain.Specification;


public class SkillSetEvaluationSpec {
    public static Specification<SkillSetEvaluation> getByCompetencyCycle(Integer cycleId) {
        return (root, query, builder) -> {
            query.orderBy(builder.desc(root.get("finalProficiencyLevel")));
            query.where(builder.equal(root.get("competencyCycle").get("id"), cycleId));
            return query.getRestriction();
        };
    }

    public static Specification<SkillSetEvaluation> getDescByCycleAndEmployee(Integer competencyCycleId, Integer employeeId) {
        return (root, query, builder) -> {
            query.orderBy(builder.desc(root.get("finalProficiencyLevel")));
            query.where(builder.equal(root.get("competencyCycle").get("id"), competencyCycleId),
                    builder.equal(root.get("employee").get("id"), employeeId));
            return query.getRestriction();
        };
    }

    public static Specification<SkillSetEvaluation> getAscByCycleAndEmployee(Integer competencyCycleId, Integer employeeId) {
        return (root, query, builder) -> {
            query.orderBy(builder.asc(root.get("finalProficiencyLevel")));
            query.where(builder.equal(root.get("competencyCycle").get("id"), competencyCycleId),
                    builder.equal(root.get("employee").get("id"), employeeId));
            return query.getRestriction();
        };
    }
}