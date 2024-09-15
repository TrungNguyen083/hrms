package com.hrms.performancemanagement.repositories;

import com.hrms.performancemanagement.model.PerformanceEvaluationOverall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface PerformanceEvaluationOverallRepository extends JpaRepository<PerformanceEvaluationOverall, Integer> ,
        JpaSpecificationExecutor<PerformanceEvaluationOverall>
{

    <T> Collection<T> findAllByEvaluateCycleId(Integer cycleId, Class<T> type);

    @Query("SELECT avg(pe.finalAssessment) FROM PerformanceEvaluationOverall pe " +
            "INNER JOIN EvaluateCycle pc ON pe.id = pc.id " +
            "WHERE pe.id IN ?1")
    Double avgEvalScoreByIdIn(List<Integer> ids);

    default <T> Collection<T> findAllByCycleId(Integer cycleId, Class<T> type) {
        return findAllByEvaluateCycleId(cycleId, type);
    }
}