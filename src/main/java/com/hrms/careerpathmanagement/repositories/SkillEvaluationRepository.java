package com.hrms.careerpathmanagement.repositories;

import com.hrms.careerpathmanagement.models.SkillEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface SkillEvaluationRepository extends JpaRepository<SkillEvaluation, Integer>, JpaSpecificationExecutor<SkillEvaluation> {
}
