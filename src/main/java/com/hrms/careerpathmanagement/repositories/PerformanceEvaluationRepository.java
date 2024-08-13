package com.hrms.careerpathmanagement.repositories;

import com.hrms.performancemanagement.model.PerformanceEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface PerformanceEvaluationRepository extends JpaRepository<PerformanceEvaluation, Integer> ,
        JpaSpecificationExecutor<PerformanceEvaluation>
{

    <T> Collection<T> findAllByEvaluateCycleId(Integer cycleId, Class<T> type);

    @Query("SELECT avg(pe.finalAssessment) FROM PerformanceEvaluation pe " +
            "INNER JOIN EvaluateCycle pc ON pe.id = pc.id " +
            "WHERE pe.id IN ?1")
    Double avgEvalScoreByIdIn(List<Integer> ids);

    default <T> Collection<T> findAllByCycleId(Integer cycleId, Class<T> type) {
        return findAllByEvaluateCycleId(cycleId, type);
    }
}