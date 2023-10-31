package com.hrms.competencymanagement.repositories;

import com.hrms.competencymanagement.models.CompetencyEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CompetencyEvaluationRepository extends JpaRepository<CompetencyEvaluation, Integer>, JpaSpecificationExecutor<CompetencyEvaluation> {
}
