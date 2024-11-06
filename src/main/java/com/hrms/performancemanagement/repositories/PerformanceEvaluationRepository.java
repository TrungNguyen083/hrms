package com.hrms.performancemanagement.repositories;

import com.hrms.global.models.EvaluateCycle;
import com.hrms.performancemanagement.model.PerformanceEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceEvaluationRepository extends JpaRepository<PerformanceEvaluation, Integer>, JpaSpecificationExecutor<PerformanceEvaluation> {
}
